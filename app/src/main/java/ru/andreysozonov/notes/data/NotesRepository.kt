package ru.andreysozonov.notes.data

import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.provider.RemoteDataProvider

class NotesRepository(private val remoteDataProvider: RemoteDataProvider) {

    fun getNotes() = remoteDataProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteDataProvider.saveNote(note)

    fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)

    fun getCurrentUser() = remoteDataProvider.getCurrentUser()

    fun deleteNote(id: String) = remoteDataProvider.deleteNote(id)

}

