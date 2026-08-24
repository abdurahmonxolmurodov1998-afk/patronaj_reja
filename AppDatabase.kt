package com.patronaj.reja.ui.screens.todayplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patronaj.reja.data.entity.Task
import com.patronaj.reja.data.entity.TaskStatus
import com.patronaj.reja.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class TodayPlanViewModel(private val repository: TaskRepository) : ViewModel() {

    private val today = LocalDate.now()

    private val _isGenerating = MutableStateFlow(true)
    val isGenerating: StateFlow<Boolean> = _isGenerating

    val todayTasks: StateFlow<List<Task>> = repository.getForDate(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensurePlanFor(today)
            _isGenerating.value = false
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isGenerating.value = true
            repository.ensurePlanFor(today)
            _isGenerating.value = false
        }
    }

    fun markCompleted(task: Task) {
        viewModelScope.launch { repository.updateStatus(task, TaskStatus.COMPLETED, LocalDate.now()) }
    }

    fun markPostponed(task: Task) {
        viewModelScope.launch { repository.updateStatus(task, TaskStatus.POSTPONED) }
    }

    fun markMissed(task: Task) {
        viewModelScope.launch { repository.updateStatus(task, TaskStatus.MISSED) }
    }

    fun repositoryTasksForPatient(patientId: Long): Flow<List<Task>> =
        repository.getForPatient(patientId)
}
