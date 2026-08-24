package com.patronaj.reja.ui.screens.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patronaj.reja.data.entity.Patient
import com.patronaj.reja.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PatientViewModel(private val repository: PatientRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val patients: StateFlow<List<Patient>> = _query
        .flatMapLatest { q -> if (q.isBlank()) repository.getActive() else repository.search(q) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun save(patient: Patient, onResult: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.save(patient)
            onResult(id)
        }
    }

    fun delete(patient: Patient) {
        viewModelScope.launch { repository.delete(patient) }
    }

    suspend fun getById(id: Long): Patient? = repository.getById(id)

    suspend fun isDuplicate(jshshir: String, medId: String, excludeId: Long): Boolean =
        repository.isDuplicate(jshshir, medId, excludeId)
}
