package ph.edu.batstateu.arasof.arasofcampusassetmapping.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.Asset
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.Room
import ph.edu.batstateu.arasof.arasofcampusassetmapping.viewmodel.CampusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomSheet(
    room: Room,
    assets: List<Asset>,
    vm: CampusViewModel,
    onAssetClick: (Asset) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
            Text(room.name, style = MaterialTheme.typography.titleLarge)
            Text(room.floor.label, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            // Computer count is IT inventory, so only maintenance sees it.
            if (vm.isMaintenance) {
                Text(if (room.computers > 0) "Computers: about ${room.computers}" else "No computers in this room")
                Spacer(Modifier.height(8.dp))
            }
            Text("Assets in this room", style = MaterialTheme.typography.labelLarge)
            if (assets.isEmpty()) Text("None recorded")
            assets.forEach { a ->
                val issue = vm.openIncidentFor(a.id)
                Row(
                    Modifier.fillMaxWidth().clickable { onAssetClick(a) }.padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(a.type.label, modifier = Modifier.weight(1f))
                    Text(
                        if (issue != null) "Issue reported" else "OK",
                        color = if (issue != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
                HorizontalDivider()
            }
        }
    }
}