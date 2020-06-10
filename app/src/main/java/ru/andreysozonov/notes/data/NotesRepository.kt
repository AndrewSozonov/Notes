package ru.andreysozonov.notes.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.andreysozonov.notes.data.entity.Color
import ru.andreysozonov.notes.data.entity.Note
import java.util.*

object NotesRepository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes = mutableListOf(
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.BLUE),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.BLUE),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.BLUE),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.BLUE),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.VIOLET),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.BLUE),
        Note(UUID.randomUUID().toString(), "Head", "Note with some text", Color.VIOLET)
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    fun saveNote(note: Note) {
        addOrReplaceNote(note)
        notesLiveData.value = notes
    }

    fun addOrReplaceNote(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }
}

