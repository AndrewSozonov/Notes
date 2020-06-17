package ru.andreysozonov.notes.ui.main

import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)