package ru.andreysozonov.notes.data

import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.provider.RemoteDataProvider

class NotesRepository(private val remoteDataProvider: RemoteDataProvider) {

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    suspend fun saveNote(note: Note) = remoteDataProvider.saveNote(note)

    suspend fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

    suspend fun getCurrentUser() = remoteDataProvider.getCurrentUser()

    suspend fun deleteNote(id: String) = remoteDataProvider.deleteNote(id)

}

