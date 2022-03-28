package com.serhii.czechair.ui.screens.map

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import com.serhii.czechair.data.models.FlightState
import com.serhii.czechair.ui.viewmodels.SharedViewModel

@ExperimentalFoundationApi
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun MapScreen(
    sharedViewModel: SharedViewModel
) {

    val mapStats by sharedViewModel.mapStats.collectAsState()
    val flightStates = mapStats?.flightStates?.sortedBy { it.callsign } ?: emptyList()

    val selectedFlightState by sharedViewModel.selectedFlightState.collectAsState()

    val configuration = LocalConfiguration.current

    val animateCamera = sharedViewModel.animateSelectedFlight
    sharedViewModel.animateSelectedFlight = false

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row {
            HorizontalFlightInfo(
                flightStates = flightStates,
                onItemClicked = { sharedViewModel.setSelectedFlightState(it) }
            )
            MapView(
                flightStates = flightStates,
                selectedFlightState = selectedFlightState,
                onMarkerClicked = { sharedViewModel.setSelectedFlightState(it) },
                animateCamera = animateCamera
            )
        }
    } else {
        Box {
            MapView(
                flightStates = flightStates,
                selectedFlightState = selectedFlightState,
                onMarkerClicked = { sharedViewModel.setSelectedFlightState(it) },
                animateCamera = animateCamera
            )
            VerticalFlightInfo(
                flightStates = flightStates,
                onItemClicked = { sharedViewModel.setSelectedFlightState(it) }
            )
        }
    }

    BackHandler(enabled = selectedFlightState != null) {
        sharedViewModel.setSelectedFlightState(null)
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun VerticalFlightInfo(
    flightStates: List<FlightState>,
    onItemClicked: (FlightState) -> Unit
) {
    MapSheetContent(
        flightStats = flightStates,
        onItemClicked = onItemClicked
    )
}

@ExperimentalMaterialApi
@Composable
fun HorizontalFlightInfo(
    flightStates: List<FlightState>,
    onItemClicked: (FlightState) -> Unit
) {
    HorizontalMapList(
        flightStats = flightStates,
        onItemClicked = onItemClicked
    )
}