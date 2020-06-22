package ru.andreysozonov.notes.ui.splash

import ru.andreysozonov.notes.ui.base.BaseViewState

class SplashViewState(isAuth: Boolean? = null, error: Throwable? = null) :
    BaseViewState<Boolean?>(isAuth, error)