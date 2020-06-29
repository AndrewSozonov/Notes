package ru.andreysozonov.notes.data.provider

import androidx.lifecycle.LiveData
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.Result
import ru.andreysozonov.notes.data.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<Result>
    fun getNoteById(id: String): LiveData<Result>
    fun saveNote(note: Note): LiveData<Result>
    fun getCurrentUser(): LiveData<User?>
    fun deleteNote(id: String): LiveData<Result>
}