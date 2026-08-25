package com.patronaj.reja.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronaj.reja.data.entity.Patient
import com.patronaj.reja.data.entity.Task
import com.patronaj.reja.logic.AgeCalculator
import com.patronaj.reja.logic.ContingentEngine
import com.patronaj.reja.ui.rememberAppViewModel
import com.patronaj.reja.ui.screens.todayplan.TodayPlanViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    patientId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val patientVm = rememberAppViewModel { app -> PatientViewModel(app.patientRepository) }
    val taskVm = rememberAppViewModel { app -> TodayPlanViewModel(app.taskRepository) }

    var patient by remember { mutableStateOf<Patient?>(null) }

    LaunchedEffect(patientId) {
        patient = patientVm.getById(patientId)
    }

    val history by taskVm.repositoryTasksForPatient(patientId).collectAsState(initial = emptyList())
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aholi kartasi") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                },
                actions = {
                    IconButton(onClick = { onEdit(patientId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Tahrirlash")
                    }
                }
            )
        }
    ) { padding ->
        val p = patient
        if (p == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(p.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        InfoRow("Yosh", AgeCalculator.displayAge(p))
                        InfoRow("Tug'ilgan sana", p.birthDate.format(dateFormatter))
                        InfoRow("Jinsi", if (p.gender.name == "AYOL") "Ayol" else "Erkak")
                        InfoRow("JSHSHIR", p.jshshir.ifBlank { "-" })
                        InfoRow("MedID", p.medId.ifBlank { "-" })
                        InfoRow("Manzil", p.address.ifBlank { "-" })
                        InfoRow("Telefon", p.phone.ifBlank { "-" })
                        InfoRow("D guruhi", if (p.dGroup.name == "NONE") "Yo'q" else p.dGroup.name)
                        InfoRow("Holati", if (p.isActive) "Faol" else "Nofaol")
                    }
                }
            }

            item {
                Text("Kontingentlar", style = MaterialTheme.typography.titleMedium)
            }
            item {
                val contingents = ContingentEngine.contingentsFor(p)
                if (contingents.isEmpty()) {
                    Text("Hech qaysi kontingentga tegishli emas", style = MaterialTheme.typography.bodySmall)
                } else {
                    FlowRowSimple(contingents.map { ContingentEngine.label(it) })
                }
            }

            item {
                Text("Tarix (rejalashtirilgan ishlar)", style = MaterialTheme.typography.titleMedium)
            }

            if (history.isEmpty()) {
                item { Text("Hali tarix yo'q", style = MaterialTheme.typography.bodySmall) }
            } else {
                items(history) { task -> TaskHistoryRow(task) }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun FlowRowSimple(items: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { AssistChip(onClick = {}, label = { Text(it) }) }
    }
}

@Composable
private fun TaskHistoryRow(task: Task) {
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(task.reason, fontWeight = FontWeight.Bold)
                Text(task.taskType, style = MaterialTheme.typography.bodySmall)
                Text(task.plannedDate.format(dateFormatter), style = MaterialTheme.typography.bodySmall)
            }
            AssistChip(onClick = {}, label = { Text(task.status.name) })
        }
    }
}
