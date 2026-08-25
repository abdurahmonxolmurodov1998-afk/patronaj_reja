package com.patronaj.reja.ui.screens.standards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronaj.reja.data.entity.Priority
import com.patronaj.reja.data.entity.Standard
import com.patronaj.reja.data.entity.TargetType
import com.patronaj.reja.logic.ContingentEngine
import com.patronaj.reja.ui.rememberAppViewModel
import com.patronaj.reja.ui.theme.ColorHigh
import com.patronaj.reja.ui.theme.ColorLow
import com.patronaj.reja.ui.theme.ColorMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardsScreen(
    onAddClick: () -> Unit,
    onEditClick: (Long) -> Unit
) {
    val vm = rememberAppViewModel { app -> StandardViewModel(app.standardRepository) }
    val standards by vm.standards.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Standartlar") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Qo'shish")
            }
        }
    ) { padding ->
        if (standards.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Standartlar yo'q. + tugmasi orqali qo'shing.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(standards, key = { it.id }) { standard ->
                    StandardCard(
                        standard = standard,
                        onClick = { onEditClick(standard.id) },
                        onToggleActive = { vm.toggleActive(standard) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StandardCard(standard: Standard, onClick: () -> Unit, onToggleActive: () -> Unit) {
    val priorityColor = when (standard.priority) {
        Priority.HIGH -> ColorHigh
        Priority.MEDIUM -> ColorMedium
        Priority.LOW -> ColorLow
    }
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(priorityColor)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(standard.name, fontWeight = FontWeight.Bold)
                val targetLabel = if (standard.targetType == TargetType.CONTINGENT) {
                    standard.contingent?.let { ContingentEngine.label(it) } ?: "-"
                } else {
                    standard.dGroup?.name ?: "-"
                }
                Text(
                    "$targetLabel • ${standard.taskType} • har ${standard.periodicityDays} kunda",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(checked = standard.isActive, onCheckedChange = { onToggleActive() })
        }
    }
}
