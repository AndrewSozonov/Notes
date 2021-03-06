package ru.andreysozonov.notes.ui.note

import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) :
    BaseViewState<NoteViewState.Data>(data, error) {

    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}