package ru.andreysozonov.notes.ui.main


import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.Result
import ru.andreysozonov.notes.ui.base.BaseViewModel


open class MainViewModel(repository: NotesRepository) : BaseViewModel<List<Note>?>() {


    private val notesChannel = repository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when (it) {
                    is Result.Success<*> -> setData(it.data as? List<Note>)
                    is Error -> setError(it)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}