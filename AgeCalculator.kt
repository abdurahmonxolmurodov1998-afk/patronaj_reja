package com.patronaj.reja.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronaj.reja.ui.rememberAppViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onGoToTodayPlan: () -> Unit) {
    val vm = rememberAppViewModel { app -> DashboardViewModel(app.taskRepository) }
    val state by vm.uiState.collectAsState()

    val todayLabel = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, EEEE", Locale("uz")))
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Patronaj Reja") }) }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(todayLabel, style = MaterialTheme.typography.titleMedium)

            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onGoToTodayPlan
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Bugungi ishlar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        StatColumn("Jami", state.totalToday.toString())
                        StatColumn("Bajarilgan", state.completedToday.toString())
                        StatColumn("Qolgan", state.remainingToday.toString())
                    }
                }
            }

            Text("Turlar bo'yicha", style = MaterialTheme.typography.titleMedium)

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(
                    listOf(
                        "Patronaj" to state.patronaj,
                        "Skrining" to state.skrining,
                        "Profilaktik ko'rik" to state.profilaktik,
                        "D nazorat" to state.dNazorat
                    )
                ) { (label, value) ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(value.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Text(label, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
