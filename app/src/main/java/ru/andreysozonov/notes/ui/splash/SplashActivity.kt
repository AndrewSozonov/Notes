package ru.andreysozonov.notes.ui.splash

import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import ru.andreysozonov.notes.ui.base.BaseActivity
import ru.andreysozonov.notes.ui.main.MainActivity

private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override val layoutRes = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            srartMainActivity()
        }
    }

    private fun srartMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }


}