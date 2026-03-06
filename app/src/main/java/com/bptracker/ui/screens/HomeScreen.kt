package com.bptracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bptracker.data.local.BPReading
import com.bptracker.domain.model.BPCategory
import com.bptracker.presentation.home.HomeViewModel
import com.bptracker.ui.theme.HealthColors
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCamera: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showManualEntryDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BP Tracker") },
                actions = {
                    IconButton(onClick = onNavigateToInsights) {
                        Icon(Icons.Default.Insights, "Insights")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCamera,
                icon = { Icon(Icons.Default.CameraAlt, "Camera") },
                text = { Text("Scan BP") }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.recentReadings.isEmpty()) {
                EmptyState(
                    onCameraClick = onNavigateToCamera,
                    onManualEntryClick = { showManualEntryDialog = true }
                )
            } else {
                ReadingsList(
                    readings = uiState.recentReadings,
                    onManualEntryClick = { showManualEntryDialog = true },
                    onViewAllClick = onNavigateToHistory
                )
            }
        }
    }

    if (showManualEntryDialog) {
        ManualEntryDialog(
            onDismiss = { showManualEntryDialog = false },
            onSave = { systolic, diastolic, pulse, notes ->
                viewModel.addManualReading(systolic, diastolic, pulse, notes)
                showManualEntryDialog = false
            }
        )
    }
}

@Composable
private fun EmptyState(
    onCameraClick: () -> Unit,
    onManualEntryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Welcome to BP Tracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start tracking your blood pressure readings",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        FilledTonalButton(
            onClick = onCameraClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.CameraAlt, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Scan with Camera")
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onManualEntryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Edit, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Manual Entry")
        }
    }
}

@Composable
private fun ReadingsList(
    readings: List<BPReading>,
    onManualEntryClick: () -> Unit,
    onViewAllClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Readings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onManualEntryClick) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add")
                }
            }
        }

        items(readings.take(10)) { reading ->
            ReadingCard(reading = reading)
        }

        if (readings.size > 10) {
            item {
                TextButton(
                    onClick = onViewAllClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View All Readings")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReadingCard(reading: BPReading) {
    val category = BPCategory.fromReading(reading.systolic, reading.diastolic)
    val categoryColor = HealthColors.getStatusColor(category.name)

    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy • hh:mm a")
    val dateTimeText = reading.timestamp.format(formatter)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${reading.systolic}/${reading.diastolic}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = categoryColor
                )
                Text(
                    text = dateTimeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (reading.notes?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = reading.notes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${reading.pulse} bpm",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ManualEntryDialog(
    onDismiss: () -> Unit,
    onSave: (Int, Int, Int?, String?) -> Unit
) {
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    var pulse by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Blood Pressure Reading") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = systolic,
                    onValueChange = { systolic = it },
                    label = { Text("Systolic (mmHg)") },
                    placeholder = { Text("120") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = diastolic,
                    onValueChange = { diastolic = it },
                    label = { Text("Diastolic (mmHg)") },
                    placeholder = { Text("80") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = pulse,
                    onValueChange = { pulse = it },
                    label = { Text("Pulse (bpm)") },
                    placeholder = { Text("72") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    placeholder = { Text("After exercise") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val sys = systolic.toIntOrNull() ?: 0
                    val dia = diastolic.toIntOrNull() ?: 0
                    val pul = pulse.toIntOrNull()
                    if (sys > 0 && dia > 0) {
                        onSave(sys, dia, pul, notes.takeIf { it.isNotEmpty() })
                    }
                },
                enabled = systolic.toIntOrNull() != null && diastolic.toIntOrNull() != null
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
