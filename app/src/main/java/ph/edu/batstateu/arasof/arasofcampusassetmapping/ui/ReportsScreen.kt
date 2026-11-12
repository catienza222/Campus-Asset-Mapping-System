package ph.edu.batstateu.arasof.arasofcampusassetmapping.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.Incident
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.IncidentStatus
import ph.edu.batstateu.arasof.arasofcampusassetmapping.viewmodel.CampusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun fmt(t: Long) = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(t))

@Composable
fun ReportsScreen(vm: CampusViewModel) {
    var filter by remember { mutableStateOf(IncidentStatus.OPEN) }
    var resolving by remember { mutableStateOf<Incident?>(null) }
    var note by remember { mutableStateOf("") }
    val list = vm.incidents.filter { it.status == filter }.sortedByDescending { it.reportedAt }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = vm::back) { Text("← Map") }
            Text("Reports", style = MaterialTheme.typography.titleLarge)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(filter == IncidentStatus.OPEN, { filter = IncidentStatus.OPEN }, { Text("Open (${vm.openCount()})") })
            FilterChip(filter == IncidentStatus.RESOLVED, { filter = IncidentStatus.RESOLVED }, { Text("Resolved") })
        }
        Spacer(Modifier.height(8.dp))
        if (list.isEmpty()) Text("Nothing here yet.")

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(list, key = { it.id }) { inc ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(inc.assetLabel, fontWeight = FontWeight.Bold)
                        Text("${inc.buildingName} · ${inc.floor.label}", style = MaterialTheme.typography.bodySmall)
                        Text(inc.description)
                        Text("Reported by ${inc.reportedBy} · ${fmt(inc.reportedAt)}", style = MaterialTheme.typography.bodySmall)
                        if (inc.status == IncidentStatus.RESOLVED) {
                            Text("Fixed by ${inc.resolvedBy} · ${fmt(inc.resolvedAt ?: 0)}", style = MaterialTheme.typography.bodySmall)
                            inc.resolutionNote?.let { Text("Note: $it") }
                        } else if (vm.isMaintenance) {
                            Spacer(Modifier.height(6.dp))
                            Button(onClick = { note = ""; resolving = inc }) { Text("Mark as fixed") }
                        } else {
                            Text("Waiting for maintenance", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }

    resolving?.let { inc ->
        AlertDialog(
            onDismissRequest = { resolving = null },
            title = { Text("Mark as fixed?") },
            text = {
                OutlinedTextField(note, { note = it }, label = { Text("Fix note (optional)") }, modifier = Modifier.fillMaxWidth())
            },
            confirmButton = { Button(onClick = { vm.resolve(inc, note); resolving = null }) { Text("Confirm") } },
            dismissButton = { TextButton(onClick = { resolving = null }) { Text("Cancel") } }
        )
    }
}