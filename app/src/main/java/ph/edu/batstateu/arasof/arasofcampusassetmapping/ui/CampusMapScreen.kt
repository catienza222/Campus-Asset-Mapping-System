package ph.edu.batstateu.arasof.arasofcampusassetmapping.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isShiftPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.batstateu.arasof.arasofcampusassetmapping.R
import ph.edu.batstateu.arasof.arasofcampusassetmapping.data.CampusData
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.Building
import ph.edu.batstateu.arasof.arasofcampusassetmapping.model.BuildingStatus
import ph.edu.batstateu.arasof.arasofcampusassetmapping.viewmodel.CampusViewModel

private const val MAX_ZOOM = 8f

/** Keeps the zoomed map from being dragged out of view. */
private fun clampOffset(o: Offset, s: Float, size: IntSize): Offset {
    val mx = size.width * (s - 1f) / 2f
    val my = size.height * (s - 1f) / 2f
    return Offset(o.x.coerceIn(-mx, mx), o.y.coerceIn(-my, my))
}

@Composable
fun CampusMapScreen(vm: CampusViewModel) {
    var picked by remember { mutableStateOf<Building?>(null) }
    var notice by remember { mutableStateOf<String?>(null) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    /** Zoom by [factor] while keeping the point [focus] (view pixels) under the finger / cursor. */
    fun zoomAt(focus: Offset, factor: Float) {
        val c = Offset(boxSize.width / 2f, boxSize.height / 2f)
        val ns = (scale * factor).coerceIn(1f, MAX_ZOOM)
        val rel = focus - c
        offset = clampOffset(rel - (rel - offset) * (ns / scale), ns, boxSize)
        scale = ns
    }

    fun reset() { scale = 1f; offset = Offset.Zero }

    /** Which building is at this view position (taking zoom and pan into account)? */
    fun buildingAt(p: Offset): Building? {
        if (boxSize.width == 0) return null
        val c = Offset(boxSize.width / 2f, boxSize.height / 2f)
        val content = c + (p - c - offset) / scale
        val k = CampusData.MAP_W / boxSize.width
        val mx = content.x * k
        val my = content.y * k
        CampusData.buildings.firstOrNull { it.contains(mx, my) }?.let { return it }
        // forgiving tap: nearest building within ~12 screen pixels
        val tol = 12f * k / scale
        return CampusData.buildings
            .filter { it.distanceToBox(mx, my) <= tol }
            .minByOrNull { it.distanceToBox(mx, my) }
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Campus map", style = MaterialTheme.typography.titleLarge)
                Text(
                    "${vm.userName} · ${if (vm.isMaintenance) "Maintenance" else "Student"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            BadgedBox(badge = { if (vm.openCount() > 0) Badge { Text("${vm.openCount()}") } }) {
                TextButton(onClick = vm::openReports) { Text("Reports") }
            }
            TextButton(onClick = vm::logout) { Text("Log out") }
        }
        Text(
            "Tap a building. Pinch, double-tap or mouse wheel to zoom; drag to move " +
                    "(Shift + wheel moves sideways).",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(6.dp))

        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            val ratio = CampusData.MAP_W / CampusData.MAP_H
            val mapW = minOf(maxWidth, maxHeight * ratio)
            val mapH = mapW / ratio

            Box(
                Modifier
                    .size(mapW, mapH)
                    .clipToBounds()
                    .onSizeChanged { boxSize = it }
                    // 1) finger pinch + drag (also mouse drag)
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, _ ->
                            val c = Offset(size.width / 2f, size.height / 2f)
                            val ns = (scale * zoom).coerceIn(1f, MAX_ZOOM)
                            val rel = centroid - c
                            val moved = rel - (rel - offset) * (ns / scale) + pan
                            scale = ns
                            offset = clampOffset(moved, ns, size)
                        }
                    }
                    // 2) laptop: mouse wheel / touchpad scroll zooms, Shift + wheel moves sideways
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                if (event.type == PointerEventType.Scroll) {
                                    val ch = event.changes.first()
                                    val d = ch.scrollDelta
                                    if (event.keyboardModifiers.isShiftPressed) {
                                        val move = if (d.x != 0f) d.x else d.y
                                        offset = clampOffset(offset + Offset(-move * 60f, 0f), scale, size)
                                        ch.consume()
                                    } else if (d.y != 0f) {
                                        zoomAt(ch.position, if (d.y < 0f) 1.2f else 1f / 1.2f)
                                        ch.consume()
                                    }
                                }
                            }
                        }
                    }
                    // 3) tap a building, double-tap to zoom
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { p ->
                                val b = buildingAt(p)
                                if (b == null) {
                                    notice = null
                                } else if (b.isTappable) {
                                    notice = null; picked = b
                                } else {
                                    notice = "${b.id}. ${b.name} is " +
                                            if (b.status == BuildingStatus.UNDER_CONSTRUCTION) "still under construction."
                                            else "not built yet."
                                }
                            },
                            onDoubleTap = { p -> if (scale < 2f) zoomAt(p, 2.5f) else reset() }
                        )
                    }
            ) {
                Box(
                    Modifier.fillMaxSize().graphicsLayer(
                        scaleX = scale, scaleY = scale,
                        translationX = offset.x, translationY = offset.y
                    )
                ) {
                    // The map picture: app/src/main/res/drawable/campus_map.png
                    Image(
                        painter = painterResource(R.drawable.campus_map),
                        contentDescription = "Campus map",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )

                    // highlight the building whose floor menu is open
                    picked?.let { b ->
                        Canvas(Modifier.fillMaxSize()) {
                            val k = size.width / CampusData.MAP_W
                            b.shapes.forEach { poly ->
                                val path = Path()
                                poly.forEachIndexed { i, (px, py) ->
                                    if (i == 0) path.moveTo(px * k, py * k) else path.lineTo(px * k, py * k)
                                }
                                path.close()
                                drawPath(path, Color(0x331A237E))
                                drawPath(path, Color(0xFF1A237E), style = Stroke(width = 4f))
                            }
                        }
                    }

                    // maintenance: red badge with the number of open issues in each building
                    if (vm.isMaintenance) {
                        val unit = mapW / CampusData.MAP_W
                        CampusData.buildings.filter { it.isTappable }.forEach { b ->
                            val open = vm.openCount(b.id)
                            if (open > 0) {
                                Box(
                                    Modifier
                                        .offset(unit * (b.x + b.w) - 9.dp, unit * b.y - 9.dp)
                                        .size(18.dp).clip(CircleShape).background(Color.Red),
                                    contentAlignment = Alignment.Center
                                ) { Text("$open", color = Color.White, fontSize = 10.sp) }
                            }
                        }
                    }
                }
            }
        }

        notice?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val mid = Offset(boxSize.width / 2f, boxSize.height / 2f)
            OutlinedButton(onClick = { zoomAt(mid, 1f / 1.5f) }) { Text("−") }
            OutlinedButton(onClick = { zoomAt(mid, 1.5f) }) { Text("+") }
            TextButton(onClick = { reset() }) { Text("Reset") }
            Text("Gray = under construction / not yet built", fontSize = 11.sp, modifier = Modifier.weight(1f))
        }
    }

    picked?.let { b ->
        AlertDialog(
            onDismissRequest = { picked = null },
            title = { Text("${b.id}. ${b.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(b.category.label, style = MaterialTheme.typography.labelMedium)
                    Text(if (b.floorCount > 1) "Which floor do you want to go to?" else "View this area:")
                    b.floors.forEach { f ->
                        OutlinedButton(
                            onClick = { picked = null; vm.openFloor(b, f) },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(f.label) }
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { picked = null }) { Text("Cancel") } }
        )
    }
}