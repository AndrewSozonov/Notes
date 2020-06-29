package ru.andreysozonov.notes.ui.note

import android.util.Log
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.Result
import ru.andreysozonov.notes.ui.base.BaseViewModel

class NoteViewModel(val notesRepository: NotesRepository) :
    BaseViewModel<NoteViewState.Data, NoteViewState>() {


    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note


    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
        Log.d("TAG", "current note SAVE CHANGES $currentNote")
    }

    override fun onCleared() {
        Log.d("TAG", "current note on cleared $currentNote")
        currentNote?.let {
            notesRepository.saveNote(it)
        }
    }

    fun loadNote(noteId: String) {
        notesRepository.getNoteById(noteId).observeForever { result ->
            result ?: return@observeForever
            when (result) {
                is Result.Success<*> -> {
                    viewStateLiveData.value =
                        NoteViewState(NoteViewState.Data(note = result.data as? Note))
                }
                is Result.Error -> {
                    viewStateLiveData.value = NoteViewState(error = result.error)
                }
            }
        }
    }

    fun deleteNote() {
        Log.d("TAG", "current note $currentNote")
        currentNote?.let {
            notesRepository.deleteNote(it.id).observeForever { result ->
                result?.let {
                    viewStateLiveData.value = when (it) {
                        is Result.Success<*> -> NoteViewState(NoteViewState.Data(isDeleted = true))
                        is Result.Error -> NoteViewState(error = it.error)
                    }
                }
            }
        }

    }
}