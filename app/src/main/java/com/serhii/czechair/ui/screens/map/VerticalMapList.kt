package com.serhii.czechair.ui.screens.map

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhii.czechair.R
import com.serhii.czechair.data.models.FlightState
import com.serhii.czechair.ui.ScrollableBottomSheet
import com.serhii.czechair.ui.theme.LARGE_PADDING

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun MapSheetContent(
    flightStats: List<FlightState>,
    onItemClicked: (FlightState) -> Unit
) {
    ScrollableBottomSheet(
        header = {
            BottomSheetHeader(flightCount = flightStats.size)
        },
        body = { closeSheet ->
            items(items = flightStats) { item ->
                if (item.isValid())
                    FlightStateItem(
                        state = item,
                        onItemClicked = {
                            onItemClicked(item)
                            closeSheet()
                        }
                    )
            }
        }
    )
}

@Composable
fun BottomSheetHeader(
    flightCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(vertical = LARGE_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(LARGE_PADDING))
        Text(
            text = stringResource(R.string.flight_count, flightCount),
            fontSize = 18.sp
        )
    }
}