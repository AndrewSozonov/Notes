package ru.andreysozonov.notes.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.andreysozonov.notes.R
import ru.andreysozonov.notes.data.errors.NoAuthException

private const val RC_SIGN_IN = 458

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

    abstract val model: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutRes?.let {
            setContentView(it)
        }
        model.getViewState().observe(this, Observer<S> { t ->
            t?.apply {
                data?.let { renderData(it) }
                error?.let { renderError(it) }
            }
        })
    }

    protected fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLoginActivity()
            else -> error.message?.let { showError(it) }
        }
    }

    abstract fun renderData(data: T)

    protected fun showError(error: String) {
        Snackbar.make(mainRecycler, error, Snackbar.LENGTH_INDEFINITE).apply {
            setAction(R.string.ok_button) { dismiss() }
            show()
        }
    }

    private fun startLoginActivity() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.common_google_signin_btn_icon_dark_normal)
                .setTheme(R.style.LoginStyle)
                .setAvailableProviders(providers)
                .build(), RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }
}