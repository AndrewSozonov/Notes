package ru.andreysozonov.notes.ui.note

import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) :
    BaseViewState<Note?>(note, error) {
}