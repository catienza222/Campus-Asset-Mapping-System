package ph.edu.batstateu.arasof.arasofcampusassetmapping.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.batstateu.arasof.arasofcampusassetmapping.data.CampusData
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.*
import ph.edu.batstateu.arasof.arasofcampusassetmapping.viewmodel.CampusViewModel

fun AssetType.color(): Color = when (this) {
    AssetType.EXIT -> Color(0xFF2E7D32)
    AssetType.FIRE_ALARM -> Color(0xFFC62828)
    AssetType.FIRE_LIGHT -> Color(0xFFF9A825)
    AssetType.HYDRANT -> Color(0xFF1565C0)
    AssetType.EXTINGUISHER -> Color(0xFFEF6C00)
    AssetType.COMPUTER -> Color(0xFF5E35B1)
}

@Composable
fun FloorPlanScreen(vm: CampusViewModel) {
    val building = vm.selectedBuilding ?: return
    val floor = vm.selectedFloor
    val data = remember(building, floor) { CampusData.floorData(building, floor) }
    var selectedAsset by remember { mutableStateOf<Asset?>(null) }
    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var onlyIssues by remember { mutableStateOf(false) }

    // Different map per user type: students never see IT assets (computers).
    val shownAssets = data.assets.filter {
        (vm.isMaintenance || it.type.studentVisible) &&
                (!onlyIssues || vm.openIncidentFor(it.id) != null)
    }
    val legendTypes = AssetType.values().filter { vm.isMaintenance || it.studentVisible }

    Column(Modifier.fillMaxSize().padding(12.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = vm::back) { Text("← Map") }
            Text("${building.id}. ${building.name}", style = MaterialTheme.typography.titleLarge)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            building.floors.forEach { f ->
                FilterChip(selected = f == floor, onClick = { vm.switchFloor(f) }, label = { Text(f.label) })
            }
        }
        if (vm.isMaintenance) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = onlyIssues, onCheckedChange = { onlyIssues = it })
                Spacer(Modifier.width(8.dp))
                Text("Show only reported issues")
            }
        }
        Spacer(Modifier.height(8.dp))

        BoxWithConstraints(
            Modifier.fillMaxWidth().aspectRatio(1.2f)
                .border(2.dp, MaterialTheme.colorScheme.outline)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            val w = maxWidth
            val h = maxHeight
            // corridor (only for buildings that have rooms)
            if (data.rooms.isNotEmpty()) Box(
                Modifier.offset(w * 0.05f, h * 0.42f).size(w * 0.9f, h * 0.16f)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) { Text("Corridor", fontSize = 9.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp)) }

            data.rooms.forEach { r ->
                Box(
                    Modifier.offset(w * r.x, h * r.y).size(w * r.w, h * r.h)
                        .border(1.dp, MaterialTheme.colorScheme.outline)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                        .clickable { selectedRoom = r }
                ) {
                    Text(r.name, fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(4.dp))
                }
            }
            shownAssets.forEach { a ->
                AssetMarker(
                    asset = a, hasIssue = vm.openIncidentFor(a.id) != null,
                    onClick = { selectedAsset = a },
                    modifier = Modifier.offset(w * a.x - 16.dp, h * a.y - 16.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Text("Legend", style = MaterialTheme.typography.labelLarge)
        legendTypes.forEach { t ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                Box(Modifier.size(20.dp).clip(CircleShape).background(t.color()), contentAlignment = Alignment.Center) {
                    Text(t.symbol, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(8.dp)); Text(t.label, fontSize = 13.sp)
            }
        }
        Text("A black ring with ! means a problem has been reported.", fontSize = 12.sp)
        Text("Tap a room for details, or tap an asset to report a problem.", fontSize = 12.sp)
    }

    selectedRoom?.let { room ->
        RoomSheet(
            room = room,
            assets = data.assets.filter { it.roomId == room.id && (vm.isMaintenance || it.type.studentVisible) },
            vm = vm,
            onAssetClick = { selectedRoom = null; selectedAsset = it },
            onDismiss = { selectedRoom = null }
        )
    }
    selectedAsset?.let { AssetDialog(it, vm) { selectedAsset = null } }
}

@Composable
private fun AssetMarker(asset: Asset, hasIssue: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Box(modifier.size(32.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier.size(26.dp).clip(CircleShape).background(asset.type.color())
                .border(if (hasIssue) 3.dp else 1.dp, if (hasIssue) Color.Black else Color.White, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) { Text(asset.type.symbol, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        if (hasIssue) {
            Box(
                Modifier.align(Alignment.TopEnd).size(13.dp).clip(CircleShape).background(Color.Black),
                contentAlignment = Alignment.Center
            ) { Text("!", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
fun AssetDialog(asset: Asset, vm: CampusViewModel, onDismiss: () -> Unit) {
    val incident = vm.openIncidentFor(asset.id)
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(asset.label) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(asset.type.label, style = MaterialTheme.typography.labelMedium)
                if (incident == null) {
                    Text(if (vm.isMaintenance) "No open issues. This asset is marked as working." else "Is something wrong with this asset?")
                    OutlinedTextField(
                        value = text, onValueChange = { text = it },
                        label = { Text("Describe the problem") }, modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text("Reported by ${incident.reportedBy}", fontWeight = FontWeight.Medium)
                    Text(incident.description)
                    if (vm.isMaintenance) {
                        OutlinedTextField(
                            value = text, onValueChange = { text = it },
                            label = { Text("Fix note (optional)") }, modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text("Maintenance has been notified and will update this once fixed.")
                    }
                }
            }
        },
        confirmButton = {
            when {
                incident == null -> Button(onClick = { vm.reportIssue(asset, text); onDismiss() }) { Text("Report problem") }
                vm.isMaintenance -> Button(onClick = { vm.resolve(incident, text); onDismiss() }) { Text("Mark as fixed") }
                else -> TextButton(onClick = onDismiss) { Text("OK") }
            }
        },
        dismissButton = { if (incident == null || vm.isMaintenance) TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}