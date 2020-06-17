package ru.andreysozonov.notes.ui.note

import androidx.lifecycle.ViewModel
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Note

class NoteViewModel(private val notesRepository: NotesRepository = NotesRepository) : ViewModel() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            notesRepository.saveNote(it)
        }
    }
}