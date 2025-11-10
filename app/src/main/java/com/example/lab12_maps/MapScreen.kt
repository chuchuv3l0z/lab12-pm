package com.example.lab12_maps

import android.Manifest
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
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
import kotlinx.coroutines.launch

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    // Ubicaciones clave
    val arequipaLocation = LatLng(-16.4040102, -71.559611)
    val yuraLocation = LatLng(-16.2520984, -71.6836503)

    // Cámara inicial en Arequipa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    // Propiedades del mapa (tipo de mapa)
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    // Estados UI
    var isMapTypeMenuExpanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado de permiso de ubicación
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    // Estado para guardar ubicación actual del usuario
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    // Launcher para pedir permiso en tiempo de ejecución
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
        if (!granted) {
            scope.launch {
                snackbarHostState.showSnackbar("Permiso de ubicación denegado.")
            }
        }
    }

    // Animación inicial hacia Yura
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

            // Marcador de ubicación actual (si existe)
            userLocation?.let { userLatLng ->
                Marker(
                    state = rememberMarkerState(position = userLatLng),
                    title = "Tu ubicación",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                )
            }
        }

        // FAB: cambiar tipo de mapa (abajo derecha)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 120.dp) // arriba del zoom
        ) {
            FloatingActionButton(
                onClick = { isMapTypeMenuExpanded = !isMapTypeMenuExpanded },
                containerColor = Color(0xFF1976D2)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Cambiar tipo de mapa",
                    tint = Color.White
                )
            }

            DropdownMenu(
                expanded = isMapTypeMenuExpanded,
                onDismissRequest = { isMapTypeMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Normal") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
                        isMapTypeMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Satélite") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.SATELLITE)
                        isMapTypeMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Terreno") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.TERRAIN)
                        isMapTypeMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Híbrido") },
                    onClick = {
                        mapProperties = mapProperties.copy(mapType = MapType.HYBRID)
                        isMapTypeMenuExpanded = false
                    }
                )
            }
        }

        // 📍 FAB: ir a mi ubicación (un poco más arriba del otro)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 200.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    if (!hasLocationPermission) {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    } else {
                        scope.launch {
                            try {
                                val location: Location? = fusedClient.awaitLastLocation()
                                if (location != null) {
                                    val latLng = LatLng(location.latitude, location.longitude)
                                    userLocation = latLng
                                    cameraPositionState.animate(
                                        update = CameraUpdateFactory.newLatLngZoom(latLng, 16f),
                                        durationMs = 1500
                                    )
                                } else {
                                    snackbarHostState.showSnackbar(
                                        "No se pudo obtener la ubicación. Prueba en dispositivo físico o activa el GPS."
                                    )
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar(
                                    "Error al obtener ubicación: ${e.message}"
                                )
                            }
                        }
                    }
                },
                containerColor = Color(0xFF009688)
            ) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = "Mi ubicación",
                    tint = Color.White
                )
            }
        }

        // Snackbar para mensajes
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
