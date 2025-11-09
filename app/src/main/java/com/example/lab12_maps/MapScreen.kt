package com.example.lab12_maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen() {
    // Ubicación de Arequipa
    val arequipaLocation = LatLng(-16.4040102, -71.559611)

    // Estado de la cámara inicial, centrada en Arequipa con zoom 12
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Marcador en Arequipa
            Marker(
                state = rememberMarkerState(position = arequipaLocation),
                title = "Arequipa, Perú"
            )
        }
    }
}
