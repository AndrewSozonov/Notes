package ru.andreysozonov.notes.ui.note

import android.util.Log
import kotlinx.coroutines.launch
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.ui.base.BaseViewModel

open class NoteViewModel(private val notesRepository: NotesRepository) :
    BaseViewModel<NoteViewState.Data>() {


    private val currentNote: Note?
        get() = getViewState().poll()?.note


    fun saveChanges(note: Note) {
        setData(NoteViewState.Data(note = note))
        Log.d("TAG", "current note SAVE CHANGES $currentNote")
    }

    override fun onCleared() {
        Log.d("TAG", "current note on cleared $currentNote")
        launch {
            currentNote?.let {
                notesRepository.saveNote(it)
                super.onCleared()
            }
        }

    }

    fun loadNote(noteId: String) {
        launch {
            try {
                notesRepository.getNoteById(noteId).let {
                    setData(NoteViewState.Data(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        Log.d("TAG", "current note $currentNote")
        launch {
            try {
                currentNote?.let { notesRepository.deleteNote(it.id) }
                setData(NoteViewState.Data(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }
}