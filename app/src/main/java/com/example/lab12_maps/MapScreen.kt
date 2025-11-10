package com.example.lab12_maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {
    // Ubicaciones clave
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val yuraLocation = LatLng(-16.2520984, -71.6836503)

    // Cámara inicial en Arequipa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    // Estado del tipo de mapa (usando MapProperties para Maps Compose 4.4.1)
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    // Estado del menú del botón flotante
    var isMenuExpanded by remember { mutableStateOf(false) }

    // Animación: mover cámara hacia Yura al iniciar
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(yuraLocation, 12f),
            durationMs = 3000
        )
    }

    // Marcadores adicionales
    val extraLocations = listOf(
        LatLng(-16.433415, -71.5442652),  // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamácola
    )

    // Polígonos
    val mallAventuraPolygon = listOf(
        LatLng(-16.432292, -71.509145),
        LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310),
        LatLng(-16.432566, -71.508853)
    )

    val parqueLambramaniPolygon = listOf(
        LatLng(-16.422704, -71.530830),
        LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110),
        LatLng(-16.423050, -71.530600)
    )

    val plazaDeArmasPolygon = listOf(
        LatLng(-16.398866, -71.536961),
        LatLng(-16.398744, -71.536529),
        LatLng(-16.399178, -71.536289),
        LatLng(-16.399299, -71.536721)
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // Mapa principal
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties
        ) {
            // Marcador principal: Arequipa (azul)
            Marker(
                state = rememberMarkerState(position = arequipaLocation),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                title = "Arequipa, Perú"
            )

            // Marcador en Yura (destino de la animación)
            Marker(
                state = rememberMarkerState(position = yuraLocation),
                title = "Yura",
                snippet = "Destino de la animación"
            )

            // Marcadores adicionales
            extraLocations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación",
                    snippet = "Punto de interés"
                )
            }

            // Polígonos
            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x550000FF),
                strokeWidth = 5f
            )
            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x550000FF),
                strokeWidth = 5f
            )
            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x550000FF),
                strokeWidth = 5f
            )

            // Polilínea: Arequipa → JLByR → Paucarpata → Zamácola
            Polyline(
                points = listOf(
                    arequipaLocation,
                    extraLocations[0],
                    extraLocations[1],
                    extraLocations[2]
                ),
                width = 8f,
                color = Color.Magenta
            )

            // Polilínea: Yura ↔ Arequipa
            Polyline(
                points = listOf(
                    yuraLocation,
                    arequipaLocation
                ),
                width = 6f,
                color = Color.Green
            )
        }

        // Botón flotante + menú para cambiar tipo de mapa
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 120.dp)
        ) {
            FloatingActionButton(
                onClick = { isMenuExpanded = !isMenuExpanded },
                containerColor = Color(0xFF1976D2)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Cambiar tipo de mapa",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Normal") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
                        isMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Satélite") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.SATELLITE)
                        isMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Terreno") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.TERRAIN)
                        isMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Híbrido") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.HYBRID)
                        isMenuExpanded = false
                    }
                )
            }
        }
    }
}
