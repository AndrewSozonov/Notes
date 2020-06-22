package ru.andreysozonov.notes.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.NoteResult
import java.lang.Exception

private const val NOTES_COLLECTION = "notes"

class FireStoreProvider : RemoteDataProvider {

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)


    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener { p0, p1 ->
            if (p1 != null) {
                result.value = NoteResult.Error(p1)
            } else if (p0 != null) {
                val notes = mutableListOf<Note>()
                for (doc: QueryDocumentSnapshot in p0) {
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.Success(notes)
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
            .addOnSuccessListener { snapshot ->
                result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
            }.addOnFailureListener {
                result.value = NoteResult.Error(it)
            }
        return result

    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document().set(note).addOnSuccessListener {
            Log.d(TAG, "Note $note is saved")
            result.value = NoteResult.Success(note)
        }.addOnFailureListener { p0 ->
            Log.d(TAG, "Error saving note $note, message: ${p0.message}")
            result.value = NoteResult.Error(p0)
        }
        return result
    }
}