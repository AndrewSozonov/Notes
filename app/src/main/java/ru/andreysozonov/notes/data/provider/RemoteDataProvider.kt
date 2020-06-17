package ru.andreysozonov.notes.data.provider

import androidx.lifecycle.LiveData
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.NoteResult

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}