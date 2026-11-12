package ph.edu.batstateu.arasof.arasofcampusassetmapping.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ph.edu.batstateu.arasof.arasofcampusassetmapping.data.CampusData
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.*

enum class Screen { LOGIN, MAP, FLOOR, REPORTS }

class CampusViewModel : ViewModel() {

    companion object {
        // DEMO ONLY. Replace with real authentication (e.g. Firebase Auth + a "maintenance" claim).
        const val MAINTENANCE_PIN = "1234"
    }

    var screen by mutableStateOf(Screen.LOGIN); private set
    var role by mutableStateOf(Role.STUDENT); private set
    var userName by mutableStateOf(""); private set
    var selectedBuilding by mutableStateOf<Building?>(null); private set
    var selectedFloor by mutableStateOf(Floor.GROUND); private set

    val incidents = mutableStateListOf<Incident>()
    private var nextId = 1

    val isMaintenance get() = role == Role.MAINTENANCE

    /** @return error message, or null on success */
    fun login(name: String, role: Role, pin: String): String? {
        if (name.isBlank()) return "Please enter your name"
        if (role == Role.MAINTENANCE && pin != MAINTENANCE_PIN) return "Incorrect maintenance PIN"
        this.userName = name.trim()
        this.role = role
        screen = Screen.MAP
        return null
    }

    fun logout() {
        userName = ""; role = Role.STUDENT; selectedBuilding = null
        screen = Screen.LOGIN
    }

    fun openFloor(b: Building, f: Floor) {
        selectedBuilding = b; selectedFloor = f; screen = Screen.FLOOR
    }

    fun switchFloor(f: Floor) { selectedFloor = f }
    fun openReports() { screen = Screen.REPORTS }

    fun back() {
        when (screen) {
            Screen.FLOOR, Screen.REPORTS -> screen = Screen.MAP
            else -> Unit
        }
    }

    fun openIncidentFor(assetId: String): Incident? =
        incidents.firstOrNull { it.assetId == assetId && it.status == IncidentStatus.OPEN }

    fun openCount(buildingId: Int? = null): Int =
        incidents.count { it.status == IncidentStatus.OPEN && (buildingId == null || it.buildingId == buildingId) }

    /** Students (and maintenance) can report a problem with an asset. */
    fun reportIssue(asset: Asset, description: String) {
        if (openIncidentFor(asset.id) != null) return
        val b = CampusData.buildings.first { it.id == asset.buildingId }
        incidents.add(
            Incident(
                id = nextId++, assetId = asset.id, assetLabel = asset.label,
                buildingId = b.id, buildingName = b.name, floor = asset.floor,
                description = description.ifBlank { "No details provided" },
                reportedBy = userName, reportedAt = System.currentTimeMillis()
            )
        )
    }

    /** Only maintenance may mark an incident as fixed. */
    fun resolve(incident: Incident, note: String) {
        if (!isMaintenance) return
        val i = incidents.indexOfFirst { it.id == incident.id }
        if (i < 0) return
        incidents[i] = incidents[i].copy(
            status = IncidentStatus.RESOLVED, resolvedBy = userName,
            resolvedAt = System.currentTimeMillis(), resolutionNote = note.ifBlank { null }
        )
    }
}