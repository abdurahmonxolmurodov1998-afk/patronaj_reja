package com.patronaj.reja.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patronaj.reja.data.entity.TaskStatus
import com.patronaj.reja.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class DashboardUiState(
    val totalToday: Int = 0,
    val completedToday: Int = 0,
    val remainingToday: Int = 0,
    val patronaj: Int = 0,
    val skrining: Int = 0,
    val profilaktik: Int = 0,
    val dNazorat: Int = 0
)

class DashboardViewModel(private val repository: TaskRepository) : ViewModel() {

    private val today = LocalDate.now()

    init {
        viewModelScope.launch { repository.ensurePlanFor(today) }
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.countForDate(today),
        repository.countForDateByStatus(today, TaskStatus.COMPLETED),
        repository.countForDateByType(today, "PATRONAJ"),
        repository.countForDateByType(today, "SKRINING"),
        repository.countForDateByType(today, "PROFILAKTIK_KORIK"),
        repository.countForDateByType(today, "D_NAZORAT")
    ) { values ->
        val total = values[0]
        val completed = values[1]
        DashboardUiState(
            totalToday = total,
            completedToday = completed,
            remainingToday = total - completed,
            patronaj = values[2],
            skrining = values[3],
            profilaktik = values[4],
            dNazorat = values[5]
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())
}
