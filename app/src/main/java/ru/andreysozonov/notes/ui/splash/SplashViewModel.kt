package ru.andreysozonov.notes.ui.splash

import kotlinx.coroutines.launch
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.errors.NoAuthException
import ru.andreysozonov.notes.ui.base.BaseViewModel

class SplashViewModel(private val repository: NotesRepository) :
    BaseViewModel<Boolean>() {

    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}





