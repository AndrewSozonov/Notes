package ru.andreysozonov.notes.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.errors.NoAuthException
import ru.andreysozonov.notes.data.model.Result
import ru.andreysozonov.notes.data.model.User

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) :
    RemoteDataProvider {

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val currentUser
            get() =  firebaseAuth.currentUser

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()


    override fun subscribeToAllNotes(): LiveData<Result> =
        MutableLiveData<Result>().apply {
            try {
                getUserNotesCollection().addSnapshotListener { snapshot, e ->
                    value = e?.let { Result.Error(it) }
                        ?: snapshot?.let {
                            val notes = it.documents.map { it.toObject(Note::class.java) }
                            Result.Success(notes)
                        }
                }
            } catch (e: Throwable) {
                value = Result.Error(e)
            }
        }

    override fun getNoteById(id: String): LiveData<Result> =
        MutableLiveData<Result>().apply {
            try {

                getUserNotesCollection().document(id).get()
                    .addOnSuccessListener {
                        value = Result.Success(it.toObject(Note::class.java))
                    }.addOnFailureListener {
                        throw it
                    }
            } catch (e: Throwable) {
                value = Result.Error(e)
            }
        }

    override fun saveNote(note: Note): LiveData<Result> =
        MutableLiveData<Result>().apply {
            try {
                getUserNotesCollection().document(note.id).set(note).addOnSuccessListener {
                    Log.d(TAG, "Note $note is saved")
                    value = Result.Success(note)
                }.addOnFailureListener {
                    Log.d(TAG, "Error saving note $note, message: ${it.message}")
                    throw it
                }
            } catch (e: Throwable) {
                value = Result.Error(e)
            }
        }

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
        }

    override fun deleteNote(id: String): LiveData<Result> =
        MutableLiveData<Result>().apply {
            getUserNotesCollection().document(id).delete().addOnSuccessListener {
                value = Result.Success(null)
            }.addOnFailureListener {
                value = Result.Error(it)
            }
        }


}