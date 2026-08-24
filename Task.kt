package com.patronaj.reja.ui.screens.patient

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.patronaj.reja.data.entity.DGroup
import com.patronaj.reja.data.entity.Gender
import com.patronaj.reja.data.entity.Patient
import com.patronaj.reja.ui.rememberAppViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientAddEditScreen(
    patientId: Long,
    onDone: () -> Unit
) {
    val vm = rememberAppViewModel { app -> PatientViewModel(app.patientRepository) }
    val context = LocalContext.current

    var loaded by remember { mutableStateOf(patientId == 0L) }

    var fullName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf(LocalDate.now().minusYears(1)) }
    var gender by remember { mutableStateOf(Gender.ERKAK) }
    var jshshir by remember { mutableStateOf("") }
    var medId by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dGroup by remember { mutableStateOf(DGroup.NONE) }
    var isActive by remember { mutableStateOf(true) }

    var duplicateWarning by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(patientId) {
        if (patientId != 0L) {
            vm.getById(patientId)?.let {
                fullName = it.fullName
                birthDate = it.birthDate
                gender = it.gender
                jshshir = it.jshshir
                medId = it.medId
                address = it.address
                phone = it.phone
                dGroup = it.dGroup
                isActive = it.isActive
            }
            loaded = true
        }
    }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (patientId == 0L) "Aholi qo'shish" else "Ma'lumotni tahrirlash") },
                navigationIcon = {
                    IconButton(onClick = onDone) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                }
            )
        }
    ) { padding ->
        if (!loaded) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = fullName, onValueChange = { fullName = it },
                label = { Text("F.I.Sh.") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = birthDate.format(dateFormatter),
                onValueChange = {},
                readOnly = true,
                label = { Text("Tug'ilgan sana") },
                modifier = Modifier.fillMaxWidth().let { m ->
                    m
                },
                trailingIcon = {
                    TextButton(onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> birthDate = LocalDate.of(y, m + 1, d) },
                            birthDate.year, birthDate.monthValue - 1, birthDate.dayOfMonth
                        ).show()
                    }) { Text("Tanlash") }
                }
            )

            Text("Jinsi:", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = gender == Gender.ERKAK, onClick = { gender = Gender.ERKAK }, label = { Text("Erkak") })
                FilterChip(selected = gender == Gender.AYOL, onClick = { gender = Gender.AYOL }, label = { Text("Ayol") })
            }

            OutlinedTextField(
                value = jshshir, onValueChange = { jshshir = it.filter { c -> c.isDigit() }.take(14) },
                label = { Text("JSHSHIR") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = medId, onValueChange = { medId = it },
                label = { Text("MedID") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = address, onValueChange = { address = it },
                label = { Text("Manzil") }, modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Telefon") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Text("D guruhi:", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(DGroup.NONE, DGroup.D1, DGroup.D2, DGroup.D3, DGroup.D4).forEach { g ->
                    FilterChip(
                        selected = dGroup == g,
                        onClick = { dGroup = g },
                        label = { Text(if (g == DGroup.NONE) "Yo'q" else g.name) }
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Faol", modifier = Modifier.weight(1f))
                Switch(checked = isActive, onCheckedChange = { isActive = it })
            }

            duplicateWarning?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (fullName.isBlank()) return@Button
                    vm.save(
                        Patient(
                            id = patientId,
                            fullName = fullName.trim(),
                            birthDate = birthDate,
                            gender = gender,
                            jshshir = jshshir.trim(),
                            medId = medId.trim(),
                            address = address.trim(),
                            phone = phone.trim(),
                            dGroup = dGroup,
                            isActive = isActive
                        )
                    ) { onDone() }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Saqlash")
            }
        }
    }
}
