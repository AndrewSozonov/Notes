package ru.andreysozonov.notes.data

import ru.andreysozonov.notes.data.entity.Note

object NotesRepository {

    val notes: List<Note> = listOf(
        Note("Head 1", "Note with some text 1", 0xfffFFE4C4.toInt()),
        Note("Head 2", "Note with some text 2", 0xfffFFE4C4.toInt()),
        Note("Head 3", "Note with some text 3", 0xfffDEB887.toInt()),
        Note("Head 4", "Note with some text 4", 0xfffDEB887.toInt()),
        Note("Head 5", "Note with some text 5", 0xfffFFE4C4.toInt()),
        Note("Head 6", "Note with some text 6", 0xfffFFE4C4.toInt()),
        Note("Head 7", "Note with some text 7", 0xfffDEB887.toInt()),
        Note("Head 8", "Note with some text 8", 0xfffDEB887.toInt()))
}