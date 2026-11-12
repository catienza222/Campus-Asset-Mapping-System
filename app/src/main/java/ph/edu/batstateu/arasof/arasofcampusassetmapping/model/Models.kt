package ph.edu.batstateu.arasof.arasofcampusassetmapping.model

enum class Role { STUDENT, MAINTENANCE }

enum class Floor(val label: String) {
    GROUND("Ground floor"), SECOND("2nd floor"), THIRD("3rd floor")
}

/** studentVisible = false -> only maintenance (IT) can see this asset type on the map */
enum class AssetType(val label: String, val symbol: String, val studentVisible: Boolean) {
    EXIT("Exit", "E", true),
    FIRE_ALARM("Fire alarm", "A", true),
    FIRE_LIGHT("Fire light", "L", true),
    HYDRANT("Fire hydrant", "H", true),
    EXTINGUISHER("Fire extinguisher", "X", true),
    COMPUTER("Computers", "PC", false)
}

enum class IncidentStatus { OPEN, RESOLVED }

enum class BuildingStatus { OPEN, UNDER_CONSTRUCTION, UNBUILT }

enum class Category(val label: String) {
    ADMINISTRATION("Administration building"),
    GENERAL("General services"),
    ALLIED("Allied service"),
    ACADEMIC("Academic building"),
    MIXED_USE("Mixed-use"),
    AUXILIARY("Auxiliary services"),
    OTHERS("Others"),
    RESEARCH("Research office")
}

/**
 * shapes = the tap area(s) of the building, traced on the campus map picture (picture pixels).
 * x, y, w, h = bounding box of all shapes.
 * roomsPerRow = 0 means an outdoor/open area with no rooms.
 * Buildings whose status is not OPEN are gray on the map and cannot be tapped.
 */
data class Building(
    val id: Int,
    val name: String,
    val category: Category,
    val x: Float, val y: Float, val w: Float, val h: Float,
    val floorCount: Int = 3,
    val roomsPerRow: Int = 3,
    val status: BuildingStatus = BuildingStatus.OPEN,
    val shapes: List<List<Pair<Float, Float>>> = emptyList()
) {
    val floors: List<Floor> get() = Floor.values().take(floorCount)
    val isTappable: Boolean get() = status == BuildingStatus.OPEN

    /** True if the point (picture pixels) is inside one of the building's shapes. */
    fun contains(px: Float, py: Float): Boolean {
        if (shapes.isEmpty()) return px in x..(x + w) && py in y..(y + h)
        return shapes.any { poly ->
            var inside = false
            var j = poly.size - 1
            for (i in poly.indices) {
                val (xi, yi) = poly[i]
                val (xj, yj) = poly[j]
                if ((yi > py) != (yj > py) && px < (xj - xi) * (py - yi) / (yj - yi) + xi) inside = !inside
                j = i
            }
            inside
        }
    }

    /** Distance from the point to the building's bounding box (0 if inside). Used for forgiving taps. */
    fun distanceToBox(px: Float, py: Float): Float {
        val dx = maxOf(x - px, 0f, px - (x + w))
        val dy = maxOf(y - py, 0f, py - (y + h))
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
}

/** Positions are fractions (0..1) of the floor-plan area. */
data class Room(
    val id: String,
    val buildingId: Int,
    val floor: Floor,
    val name: String,
    val x: Float, val y: Float, val w: Float, val h: Float,
    val computers: Int
)

data class Asset(
    val id: String,
    val buildingId: Int,
    val floor: Floor,
    val type: AssetType,
    val x: Float, val y: Float,
    val label: String,
    val roomId: String? = null
)

data class Incident(
    val id: Int,
    val assetId: String,
    val assetLabel: String,
    val buildingId: Int,
    val buildingName: String,
    val floor: Floor,
    val description: String,
    val status: IncidentStatus = IncidentStatus.OPEN,
    val reportedBy: String,
    val reportedAt: Long,
    val resolvedBy: String? = null,
    val resolvedAt: Long? = null,
    val resolutionNote: String? = null
)