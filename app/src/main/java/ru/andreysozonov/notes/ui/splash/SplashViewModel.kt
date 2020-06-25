package ru.andreysozonov.notes.ui.splash

import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.errors.NoAuthException
import ru.andreysozonov.notes.ui.base.BaseViewModel

class SplashViewModel(private val repository: NotesRepository) :
    BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(isAuth = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}


