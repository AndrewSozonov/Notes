package ru.andreysozonov.notes.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.data.model.Result
import ru.andreysozonov.notes.ui.note.NoteViewModel
import ru.andreysozonov.notes.ui.note.NoteViewState

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: NotesRepository = mockk<NotesRepository>()
    private val noteLiveData = MutableLiveData<Result>()
    private lateinit var viewModel: NoteViewModel
    private val testNote = Note("1", "title", "note")

    @Before
    fun setUp() {
        every { mockRepository.getNoteById(any()) } returns noteLiveData
        every { mockRepository.deleteNote(any()) } returns noteLiveData
        viewModel = NoteViewModel(mockRepository)

    }

    @Test
    fun `should call getNoteById() once`() {
        mockRepository.getNoteById(testNote.id)
        verify(exactly = 1) { mockRepository.getNoteById(testNote.id) }
    }

    @Test
    fun `should return note`() {
        var result: Note? = null

        mockRepository.getNoteById(testNote.id)
            .observeForever { result = (it as? Result.Success<Note>)?.data }
        noteLiveData.value = Result.Success(data = testNote)

        assertEquals(testNote, result)
    }

    @Test
    fun `should call deleteNote() once`() {
        mockRepository.deleteNote(testNote.id)
        verify(exactly = 1) { mockRepository.deleteNote(testNote.id) }
    }

    @Test
    fun `should return isDeleted true`() {
        var result: Boolean? = null
        val testResult = true

        mockRepository.deleteNote(testNote.id).observeForever {
            result = (it as? Result.Success<NoteViewState.Data>)?.data?.isDeleted
        }
        noteLiveData.value = Result.Success(NoteViewState.Data(isDeleted = testResult))
        assertEquals(result, testResult)
    }
}