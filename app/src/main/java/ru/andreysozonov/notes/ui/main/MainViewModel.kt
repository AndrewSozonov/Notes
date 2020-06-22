package ru.andreysozonov.notes.ui.main


import androidx.lifecycle.Observer
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.NoteResult
import ru.andreysozonov.notes.ui.base.BaseViewModel


class MainViewModel(repository: NotesRepository = NotesRepository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            if (t == null) return
            when (t) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }

    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}