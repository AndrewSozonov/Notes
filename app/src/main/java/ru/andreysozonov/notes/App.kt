package ru.andreysozonov.notes

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.android.startKoin
import ru.andreysozonov.notes.di.appModule
import ru.andreysozonov.notes.di.mainModule
import ru.andreysozonov.notes.di.noteModule
import ru.andreysozonov.notes.di.splashModule

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}