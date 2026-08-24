package com.patronaj.reja.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronaj.reja.PatronajRejaApp

/** Compose ekranlarida ViewModel'ni Application repositorylari bilan yaratish uchun kichik yordamchi. */
@Composable
fun currentApp(): PatronajRejaApp {
    val context = LocalContext.current.applicationContext
    return context as PatronajRejaApp
}

class GenericViewModelFactory(private val creator: () -> ViewModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}

@Composable
inline fun <reified T : ViewModel> rememberAppViewModel(noinline creator: (PatronajRejaApp) -> T): T {
    val app = currentApp()
    return viewModel(factory = GenericViewModelFactory { creator(app) })
}
