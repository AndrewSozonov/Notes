package ru.andreysozonov.notes.ui.main


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.andreysozonov.notes.R
import ru.andreysozonov.notes.data.entity.Note
import ru.andreysozonov.notes.ui.base.BaseActivity
import ru.andreysozonov.notes.ui.note.NoteActivity
import ru.andreysozonov.notes.ui.splash.SplashActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>(), LogoutDialog.LogoutListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override val model: MainViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_main
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        initRecyclerView()
        fab.setOnClickListener { openNoteScreen(null) }
    }

    fun initRecyclerView() {
        adapter = MainAdapter(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })
        mainRecycler.layoutManager = GridLayoutManager(this, 1)
        mainRecycler.adapter = adapter
    }

    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notes = data

    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note?.id, adapter.itemCount)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        MenuInflater(this).inflate(R.menu.menu_main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.logout -> showLogoutDialog().let { true }
            else -> false
        }

    private fun showLogoutDialog() {
        supportFragmentManager.findFragmentByTag(LogoutDialog.TAG)
            ?: LogoutDialog.createInstance().show(supportFragmentManager, LogoutDialog.TAG)

    }

    override fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }
}
