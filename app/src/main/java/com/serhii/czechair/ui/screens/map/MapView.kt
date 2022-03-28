package com.serhii.czechair.ui.screens.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.serhii.czechair.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.serhii.czechair.data.models.FlightState
import com.serhii.czechair.utils.bitmapDescriptorFromVector
import kotlinx.coroutines.launch

@Composable
fun MapView(
    flightStates: List<FlightState>,
    selectedFlightState: FlightState? = null,
    onMarkerClicked: (FlightState?) -> Unit,
    animateCamera: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val planeIconRes = if (isSystemInDarkTheme()) R.drawable.ic_plane_gray else R.drawable.ic_plane_blue_gray
    val planeIcon = LocalContext.current.bitmapDescriptorFromVector(planeIconRes)
    val selectedPlaneIconRes = if (isSystemInDarkTheme()) R.drawable.ic_plane_purple else R.drawable.ic_plane_blue
    val selectedPlaneIcon = LocalContext.current.bitmapDescriptorFromVector(selectedPlaneIconRes)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(49.8175, 15.4730), 6f)
    }

    selectedFlightState?.let {
        scope.launch {
            if (animateCamera)
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            it.latitude ?: 0.0,
                            it.longitude ?: 0.0
                        ),
                        8f
                    )
                )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            compassEnabled = false,
            mapToolbarEnabled = false,
            zoomControlsEnabled = false
        ),
        properties = MapProperties(
            mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                LocalContext.current,
                if (isSystemInDarkTheme()) R.raw.map_night_mode else R.raw.map_light_mode
            )
        ),
        onMapClick = { onMarkerClicked(null) }
    ) {
        flightStates.forEach {
            if (it.isValid()) {
                val latLng = LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0)
                Marker(
                    position = latLng,
                    icon = if (it.icao24 == selectedFlightState?.icao24) selectedPlaneIcon else planeIcon,
                    title = it.toString(),
                    infoWindowAnchor = Offset(0.5f, 0.5f),
                    anchor = Offset(0.5f, 0.5f),
                    rotation = it.true_track?.toFloat() ?: 0f,
                    onClick = { marker ->
                        marker.showInfoWindow()
                        onMarkerClicked(it)
                        false
                    }
                )
            }
        }
    }
}