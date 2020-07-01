package ru.andreysozonov.notes.ui.note

import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import ru.andreysozonov.notes.R
import ru.andreysozonov.notes.common.getColorInt
import ru.andreysozonov.notes.data.entity.Color
import ru.andreysozonov.notes.data.entity.Note


class NoteActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val model: NoteViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("1", "title", "body")

    @Before
    fun setup() {

        loadKoinModules(
            listOf(
                module {
                    viewModel(override = true) { model }
                })
        )

        every { model.getViewState() } returns viewStateLiveData
        //every { model.loadNote(any()) } returns mockk()
        //every { model.saveChanges(any()) } just runs
        //every {model.deleteNote()} just runs
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(NoteViewState(NoteViewState.Data(false, testNote)))

        /*Intent().apply{
            putExtra(NoteActivity::class.java.name + "Extra.note", testNote.id)
        }.let { activityTestRule.launchActivity(it) }*/
    }


    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())

        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.palette)).perform(click()).perform(click())

        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(Color.BLUE))).perform(click())

        val colorInt = Color.BLUE.getColorInt(activityTestRule.activity)

        onView(withId(R.id.toolbar)).check { view, _ ->
            assertTrue(
                "toolbar background color does not match",
                (view.background as? ColorDrawable)?.color == colorInt
            )
        }
    }

    @Test
    fun should_call_viewModel_loadNote() {
        verify(exactly = 1) { model.loadNote(testNote.id) }
    }


    @After
    fun tearDown() {
        stopKoin()
    }
}