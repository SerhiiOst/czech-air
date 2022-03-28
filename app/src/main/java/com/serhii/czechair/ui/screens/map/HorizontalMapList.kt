package com.serhii.czechair.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhii.czechair.R
import com.serhii.czechair.data.models.FlightState
import com.serhii.czechair.ui.theme.HORIZONTAL_LIST_HEADER
import com.serhii.czechair.ui.theme.flightListColor

@ExperimentalMaterialApi
@Composable
fun HorizontalMapList(
    flightStats: List<FlightState>,
    onItemClicked: (FlightState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MaterialTheme.colors.flightListColor)
    ) {
        ListHeader(flightCount = flightStats.size)
        LazyColumn {
            items(items = flightStats) { item ->
                if (item.isValid())
                    FlightStateItem(
                        state = item,
                        onItemClicked = {
                            onItemClicked(item)
                        }
                    )
            }
        }
    }
}

@Composable
fun ListHeader(
    flightCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HORIZONTAL_LIST_HEADER),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.flight_count, flightCount),
            fontSize = 18.sp
        )
    }
}