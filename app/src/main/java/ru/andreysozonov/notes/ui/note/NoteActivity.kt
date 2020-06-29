package ru.andreysozonov.notes.ui.note

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.andreysozonov.notes.R
import ru.andreysozonov.notes.common.getColorInt
import ru.andreysozonov.notes.data.entity.Color
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "Extra.note"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"
        private const val ITEM_COUNT = "items"

        fun getStartIntent(context: Context, id: String?, itemCount: Int): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, id)
            intent.putExtra(ITEM_COUNT, itemCount)
            return intent
        }
    }

    override val model: NoteViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    private var color: Color = Color.WHITE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (note != null) {

            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)
        } else {
            getString(R.string.new_note_title)
        }
        val id = intent.getStringExtra(EXTRA_NOTE)

        id?.let { model.loadNote(it) }
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


        note?.let {
            titleEt.setText(note?.title ?: "")
            bodyEt.setText(note?.note ?: "")
            titleEt.setSelection(titleEt.text?.length ?: 0)
            bodyEt.setSelection(bodyEt.text?.length ?: 0)
            setToolbarColor(note!!.color)
        }

        colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            saveNote()
        }

        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        menuInflater.inflate(R.menu.note_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed().let { true }
            R.id.palette -> togglePalette().let { true }
            R.id.delete -> deleteNote().let { true }
            else -> super.onOptionsItemSelected(item)
        }

    private fun saveNote() {
        if (titleEt.text == null || titleEt.text!!.length < 3) return

        note = note?.copy(
            title = titleEt.text.toString(),
            note = bodyEt.text.toString(),
            lastChanged = Date(),
            color = color
        )
            ?: createNewNote()

        note?.let {
            model.saveChanges(it)
        }
    }

    fun deleteNote() {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_menu_message)
            .setPositiveButton(R.string.note_delete_ok) { dialog, which -> model.deleteNote() }
            .setNegativeButton(R.string.note_delete_cancel) { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun createNewNote(): Note {
        return Note(
            UUID.randomUUID().toString(),
            titleEt.text.toString(),
            bodyEt.text.toString()
        )
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        this.note = data.note
        initView()
    }

    private fun setToolbarColor(color: Color) {
        toolbar.setBackgroundColor(color.getColorInt(this))
    }

    fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }
}