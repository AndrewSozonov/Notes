package ru.andreysozonov.notes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_note.*
import ru.andreysozonov.notes.R
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.entity.Color
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    private val TAG = "NoteActivity"

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "Extra.note"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun getStartIntent(context: Context, note: Note?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            return intent
        }
    }


    override val viewModel: NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (note != null) {

            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)
        } else {
            getString(R.string.new_note_title)
        }
        note = intent.getParcelableExtra(EXTRA_NOTE)
        //Log.d(TAG, "noteId: $noteId")

        /*noteId?.let {id -> viewModel.loadNote(id) } ?: let {
            //supportActionBar?.title = getString(R.string.new_note_title)

        }*/

        initView()
    }

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            saveNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }


    private fun initView() {

        titleEt.removeTextChangedListener(textChangeListener)
        bodyEt.removeTextChangedListener(textChangeListener)

        Log.d(TAG, "note: ${note?.title} ${note?.id}")
        if (note != null) {
            titleEt.setText(note?.title ?: "")
            bodyEt.setText(note?.note ?: "")
            val color = when (note!!.color) {
                Color.WHITE -> R.color.color_white
                Color.VIOLET -> R.color.color_violet
                Color.YELLOW -> R.color.color_yellow
                Color.RED -> R.color.color_red
                Color.PINK -> R.color.color_pink
                Color.GREEN -> R.color.color_green
                Color.BLUE -> R.color.color_blue
            }
            toolbar.setBackgroundColor(color)
        }

        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }

    private fun saveNote() {
        if (titleEt.text == null || titleEt.text!!.length < 3) return

        note = note?.copy(
            title = titleEt.text.toString(),
            note = bodyEt.text.toString(),
            lastChanged = Date()
        ) ?: createNewNote()

        note?.let {
            viewModel.saveChanges(it)
        }
    }

    private fun createNewNote(): Note {

        return Note(
            UUID.randomUUID().toString(),
            titleEt.text.toString(),
            bodyEt.text.toString()
        )
    }

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }
}