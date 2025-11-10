package com.example.lab12_maps

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val yuraLocation = LatLng(-16.2520984, -71.6836503)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    // Animar cámara hacia Yura
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(yuraLocation, 12f),
            durationMs = 3000
        )
    }

    // Marcadores adicionales
    val extraLocations = listOf(
        LatLng(-16.433415, -71.5442652), // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamácola
    )

    // Polígonos (Mall Aventura, Lambramani, Plaza de Armas)
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
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Marcador principal en Arequipa (azul)
            Marker(
                state = rememberMarkerState(position = arequipaLocation),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                title = "Arequipa, Perú"
            )

            // Marcador en Yura (destino animación)
            Marker(
                state = rememberMarkerState(position = yuraLocation),
                title = "Yura",
                snippet = "Destino animación de cámara"
            )

            // Marcadores extra
            extraLocations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación",
                    snippet = "Punto de interés"
                )
            }

            // Polígono Plaza de Armas
            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x550000FF),
                strokeWidth = 5f
            )

            // Polígono Parque Lambramani
            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x550000FF),
                strokeWidth = 5f
            )

            // Polígono Mall Aventura
            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color(0x550000FF),
                strokeWidth = 5f
            )

            // Ejemplo de polilínea: conectar Arequipa → JLByR → Paucarpata → Zamácola
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

            // Otra polilínea: Yura ↔ Arequipa
            Polyline(
                points = listOf(
                    yuraLocation,
                    arequipaLocation
                ),
                width = 6f,
                color = Color.Green
            )
        }
    }
}
