package com.serhii.czechair.ui.screens.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.serhii.czechair.R
import com.serhii.czechair.data.models.FlightState
import com.serhii.czechair.ui.theme.LARGER_PADDING
import com.serhii.czechair.ui.theme.LARGEST_PADDING

@ExperimentalMaterialApi
@Composable
fun FlightStateItem(
    state: FlightState,
    onItemClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() })
    {
        Row(
            modifier = Modifier
                .padding(vertical = LARGER_PADDING, horizontal = LARGEST_PADDING),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.rotate(state.true_track?.toFloat() ?: 0f),
                painter = painterResource(id = R.drawable.ic_plane),
                contentDescription = stringResource(R.string.plane_icon)
            )
            Spacer(modifier = Modifier.width(LARGEST_PADDING))
            Text(
                text = state.toString(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}