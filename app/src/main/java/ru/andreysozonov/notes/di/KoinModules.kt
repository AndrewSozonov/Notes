package ru.andreysozonov.notes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ru.andreysozonov.notes.data.NotesRepository
import ru.andreysozonov.notes.data.provider.FireStoreProvider
import ru.andreysozonov.notes.data.provider.RemoteDataProvider
import ru.andreysozonov.notes.ui.main.MainViewModel
import ru.andreysozonov.notes.ui.note.NoteViewModel
import ru.andreysozonov.notes.ui.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}