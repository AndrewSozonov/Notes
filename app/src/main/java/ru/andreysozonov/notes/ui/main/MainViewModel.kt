package ru.andreysozonov.notes.ui.main


import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.Result
import ru.andreysozonov.notes.ui.base.BaseViewModel


open class MainViewModel(repository: NotesRepository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<Result> {
        override fun onChanged(t: Result?) {
            if (t == null) return
            when (t) {
                is Result.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as List<Note>)
                }
                is Result.Error -> {
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

    @VisibleForTesting
    public override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}