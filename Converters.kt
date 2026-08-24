package com.patronaj.reja.ui.screens.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronaj.reja.data.entity.DGroup
import com.patronaj.reja.data.entity.Patient
import com.patronaj.reja.logic.AgeCalculator
import com.patronaj.reja.ui.rememberAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(
    onAddClick: () -> Unit,
    onPatientClick: (Long) -> Unit
) {
    val vm = rememberAppViewModel { app -> PatientViewModel(app.patientRepository) }
    val patients by vm.patients.collectAsState()
    val query by vm.query.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Aholi") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Qo'shish")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = query,
                onValueChange = vm::onQueryChange,
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                placeholder = { Text("F.I.Sh., JSHSHIR yoki MedID bo'yicha qidirish") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            if (patients.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Bemorlar topilmadi")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(patients, key = { it.id }) { patient ->
                        PatientRow(patient, onClick = { onPatientClick(patient.id) })
                    }
                    item { Spacer(Modifier.height(72.dp)) }
                }
            }
        }
    }
}

@Composable
private fun PatientRow(patient: Patient, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(patient.fullName.take(1).uppercase(), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(patient.fullName, fontWeight = FontWeight.Bold)
                Text(
                    "${AgeCalculator.displayAge(patient)} • ${if (patient.gender.name == "AYOL") "Ayol" else "Erkak"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (patient.dGroup != DGroup.NONE) {
                AssistChip(onClick = {}, label = { Text(patient.dGroup.name) })
            }
        }
    }
}
