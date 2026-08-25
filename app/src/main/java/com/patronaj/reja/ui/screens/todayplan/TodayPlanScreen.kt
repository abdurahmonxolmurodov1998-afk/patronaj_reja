package com.patronaj.reja.ui.screens.todayplan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronaj.reja.data.entity.Patient
import com.patronaj.reja.data.entity.Priority
import com.patronaj.reja.data.entity.Task
import com.patronaj.reja.data.entity.TaskStatus
import com.patronaj.reja.repository.PatientRepository
import com.patronaj.reja.ui.rememberAppViewModel
import com.patronaj.reja.ui.theme.ColorHigh
import com.patronaj.reja.ui.theme.ColorLow
import com.patronaj.reja.ui.theme.ColorMedium
import com.patronaj.reja.ui.screens.patient.PatientViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.clickable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayPlanScreen(onPatientClick: (Long) -> Unit) {
    val vm = rememberAppViewModel { app -> TodayPlanViewModel(app.taskRepository) }
    val patientVm = rememberAppViewModel { app -> PatientViewModel(app.patientRepository) }

    val tasks by vm.todayTasks.collectAsState()
    val isGenerating by vm.isGenerating.collectAsState()

    // patientId -> Patient keshi, task kartalarida ism/yosh ko'rsatish uchun
    val patientCache = remember { mutableStateMapOf<Long, Patient>() }
    LaunchedEffect(tasks) {
        tasks.map { it.patientId }.distinct().forEach { id ->
            if (!patientCache.containsKey(id)) {
                patientVm.getById(id)?.let { patientCache[id] = it }
            }
        }
    }

    val todayLabel = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("uz")))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bugungi reja — $todayLabel") },
                actions = {
                    IconButton(onClick = { vm.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Yangilash")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (isGenerating) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (tasks.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Bugun uchun rejalashtirilgan ish yo'q")
                }
            } else {
                val grouped = tasks.groupBy { it.priority }
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(Priority.HIGH, Priority.MEDIUM, Priority.LOW).forEach { prio ->
                        val list = grouped[prio].orEmpty()
                        if (list.isNotEmpty()) {
                            item { PriorityHeader(prio, list.size) }
                            items(list, key = { it.id }) { task ->
                                TaskCard(
                                    task = task,
                                    patient = patientCache[task.patientId],
                                    onClick = { onPatientClick(task.patientId) },
                                    onComplete = { vm.markCompleted(task) },
                                    onPostpone = { vm.markPostponed(task) },
                                    onMissed = { vm.markMissed(task) }
                                )
                            }
                        }
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
private fun PriorityHeader(priority: Priority, count: Int) {
    val (emoji, label, color) = when (priority) {
        Priority.HIGH -> Triple("🔴", "MUHIM", ColorHigh)
        Priority.MEDIUM -> Triple("🟠", "O'RTA", ColorMedium)
        Priority.LOW -> Triple("🟢", "ODDIY", ColorLow)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$emoji $label", fontWeight = FontWeight.Bold, color = color, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.width(6.dp))
        Text("($count)", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun TaskCard(
    task: Task,
    patient: Patient?,
    onClick: () -> Unit,
    onComplete: () -> Unit,
    onPostpone: () -> Unit,
    onMissed: () -> Unit
) {
    val priorityColor = when (task.priority) {
        Priority.HIGH -> ColorHigh
        Priority.MEDIUM -> ColorMedium
        Priority.LOW -> ColorLow
    }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(priorityColor))
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f).clickable(onClick)) {
                    Text(patient?.fullName ?: "Bemor #${task.patientId}", fontWeight = FontWeight.Bold)
                    Text(task.taskType, style = MaterialTheme.typography.bodySmall)
                }
                if (task.status != TaskStatus.PLANNED) {
                    AssistChip(onClick = {}, label = { Text(task.status.name) })
                }
            }
            Spacer(Modifier.height(4.dp))
            Text("Sabab: ${task.reason}", style = MaterialTheme.typography.bodySmall)

            if (task.status == TaskStatus.PLANNED) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onComplete, colors = ButtonDefaults.buttonColors(containerColor = ColorLow)) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Bajarildi")
                    }
                    OutlinedButton(onClick = onPostpone) {
                        Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Kechiktirish")
                    }
                    OutlinedButton(onClick = onMissed) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Bajarilmadi")
                    }
                }
            }
        }
    }
}

private fun Modifier.clickable(onClick: () -> Unit): Modifier =
    this.then(androidx.compose.foundation.clickable(onClick = onClick))
