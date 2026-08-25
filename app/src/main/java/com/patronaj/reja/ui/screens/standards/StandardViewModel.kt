package com.patronaj.reja.ui.screens.standards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patronaj.reja.data.entity.Standard
import com.patronaj.reja.repository.StandardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StandardViewModel(private val repository: StandardRepository) : ViewModel() {

    val standards: StateFlow<List<Standard>> = repository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun save(standard: Standard) {
        viewModelScope.launch { repository.save(standard) }
    }

    fun delete(standard: Standard) {
        viewModelScope.launch { repository.delete(standard) }
    }

    fun toggleActive(standard: Standard) {
        viewModelScope.launch { repository.save(standard.copy(isActive = !standard.isActive)) }
    }

    suspend fun getById(id: Long): Standard? = repository.getById(id)
}
