package ru.andreysozonov.notes.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.Result
import ru.andreysozonov.notes.data.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): ReceiveChannel<Result>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(id: String): Unit
}