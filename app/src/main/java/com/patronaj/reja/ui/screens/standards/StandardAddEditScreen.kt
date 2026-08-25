package com.patronaj.reja.ui.screens.standards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.patronaj.reja.data.entity.Contingent
import com.patronaj.reja.data.entity.DGroup
import com.patronaj.reja.data.entity.GenderFilter
import com.patronaj.reja.data.entity.Priority
import com.patronaj.reja.data.entity.Standard
import com.patronaj.reja.data.entity.TargetType
import com.patronaj.reja.logic.ContingentEngine
import com.patronaj.reja.ui.rememberAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardAddEditScreen(
    standardId: Long,
    onDone: () -> Unit
) {
    val vm = rememberAppViewModel { app -> StandardViewModel(app.standardRepository) }

    var loaded by remember { mutableStateOf(standardId == 0L) }
    var existing by remember { mutableStateOf<Standard?>(null) }

    var name by remember { mutableStateOf("") }
    var taskType by remember { mutableStateOf("PATRONAJ") }
    var targetType by remember { mutableStateOf(TargetType.CONTINGENT) }
    var contingent by remember { mutableStateOf(Contingent.C0_1) }
    var dGroup by remember { mutableStateOf(DGroup.D1) }
    var genderFilter by remember { mutableStateOf(GenderFilter.ALL) }
    var periodicityDays by remember { mutableStateOf("30") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(standardId) {
        if (standardId != 0L) {
            val s = vm.getById(standardId)
            existing = s
            s?.let {
                name = it.name
                taskType = it.taskType
                targetType = it.targetType
                it.contingent?.let { c -> contingent = c }
                it.dGroup?.let { d -> dGroup = d }
                genderFilter = it.genderFilter
                periodicityDays = it.periodicityDays.toString()
                priority = it.priority
                isActive = it.isActive
            }
            loaded = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (standardId == 0L) "Yangi standart" else "Standartni tahrirlash") },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if (standardId != 0L) {
                        IconButton(onClick = {
                            existing?.let { vm.delete(it) }
                            onDone()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "O'chirish")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (!loaded) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Standart nomi") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = taskType, onValueChange = { taskType = it },
                label = { Text("Ish turi (masalan: PATRONAJ, SKRINING, EMLASH)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Nimaga qo'llaniladi:", style = MaterialTheme.typography.labelLarge)
            SingleChoiceRow(
                options = listOf(TargetType.CONTINGENT to "Kontingent (yosh)", TargetType.D_GROUP to "D guruh"),
                selected = targetType,
                onSelect = { targetType = it }
            )

            if (targetType == TargetType.CONTINGENT) {
                Text("Kontingent:", style = MaterialTheme.typography.labelLarge)
                DropdownField(
                    label = ContingentEngine.label(contingent),
                    options = Contingent.entries.map { it to ContingentEngine.label(it) },
                    onSelect = { contingent = it }
                )
            } else {
                Text("D guruh:", style = MaterialTheme.typography.labelLarge)
                DropdownField(
                    label = dGroup.name,
                    options = listOf(DGroup.D1, DGroup.D2, DGroup.D3, DGroup.D4).map { it to it.name },
                    onSelect = { dGroup = it }
                )
            }

            Text("Jins filtri:", style = MaterialTheme.typography.labelLarge)
            SingleChoiceRow(
                options = listOf(
                    GenderFilter.ALL to "Barchasi",
                    GenderFilter.ERKAK to "Erkak",
                    GenderFilter.AYOL to "Ayol"
                ),
                selected = genderFilter,
                onSelect = { genderFilter = it }
            )

            OutlinedTextField(
                value = periodicityDays,
                onValueChange = { periodicityDays = it.filter { c -> c.isDigit() } },
                label = { Text("Davriylik (kunlarda)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Ustuvorlik:", style = MaterialTheme.typography.labelLarge)
            SingleChoiceRow(
                options = listOf(Priority.HIGH to "Yuqori", Priority.MEDIUM to "O'rta", Priority.LOW to "Past"),
                selected = priority,
                onSelect = { priority = it }
            )

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text("Faol", modifier = Modifier.weight(1f))
                Switch(checked = isActive, onCheckedChange = { isActive = it })
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val standard = Standard(
                        id = standardId,
                        name = name.ifBlank { "Nomsiz standart" },
                        taskType = taskType.ifBlank { "PATRONAJ" },
                        targetType = targetType,
                        contingent = if (targetType == TargetType.CONTINGENT) contingent else null,
                        dGroup = if (targetType == TargetType.D_GROUP) dGroup else null,
                        genderFilter = genderFilter,
                        periodicityDays = periodicityDays.toIntOrNull() ?: 30,
                        priority = priority,
                        isActive = isActive
                    )
                    vm.save(standard)
                    onDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Saqlash")
            }
        }
    }
}

@Composable
private fun <T> SingleChoiceRow(options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (value, label) ->
            FilterChip(
                selected = value == selected,
                onClick = { onSelect(value) },
                label = { Text(label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownField(label: String, options: List<Pair<T, String>>, onSelect: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (value, text) ->
                DropdownMenuItem(text = { Text(text) }, onClick = {
                    onSelect(value)
                    expanded = false
                })
            }
        }
    }
}
