package com.serhii.czechair.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.serhii.czechair.ui.theme.LARGEST_PADDING
import com.serhii.czechair.ui.theme.VERTICAL_LIST_HEADER
import com.serhii.czechair.ui.theme.flightListColor
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class States {
    EXPANDED,
    COLLAPSED
}

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ScrollableBottomSheet(
    header: @Composable () -> Unit,
    body: LazyListScope.(() -> Unit) -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = States.COLLAPSED)
    val scrollState = rememberLazyListState()
    var cornerSize by remember { mutableStateOf(LARGEST_PADDING) }

    val scope = rememberCoroutineScope()
    cornerSize = if(swipeableState.offset.value > 100) LARGEST_PADDING else 0.dp
    //for the offset
    val headerSize = LocalDensity.current.run { VERTICAL_LIST_HEADER.toPx() }

    BoxWithConstraints {
        val constraintsScope = this
        val maxHeight = with(LocalDensity.current) {
            constraintsScope.maxHeight.toPx()
        }

        val connection = remember {
            object : NestedScrollConnection {

                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        swipeableState.performDrag(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return swipeableState.performDrag(delta).toOffset()
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    return if (
                        available.y < 0
                        && scrollState.firstVisibleItemIndex == 0
                        && scrollState.firstVisibleItemScrollOffset == 0
                    ) {
                        swipeableState.performFling(available.y)
                        available
                    } else {
                        Velocity.Zero
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    swipeableState.performFling(velocity = available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }

        Box(
            Modifier
                .nestedScroll(connection)
                .offset {
                    IntOffset(
                        0,
                        swipeableState.offset.value.roundToInt()
                    )
                }
        ) {
            CompositionLocalProvider(
                LocalOverScrollConfiguration provides null,
            ) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = cornerSize, topEnd = cornerSize))
                        .alpha(0.9f)
                        .background(MaterialTheme.colors.flightListColor)
                        .swipeable(
                            state = swipeableState,
                            orientation = Orientation.Vertical,
                            anchors = mapOf(
                                0f to States.EXPANDED,
                                maxHeight - headerSize to States.COLLAPSED,
                            )
                        )
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                //close the bottom sheet
                                scope.launch {
                                    swipeableState.performFling(velocity = dragAmount.y)
                                }
                                swipeableState.performDrag(dragAmount.y)
                            }
                        }
                ) {
                    header()
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = scrollState
                    ) {
                        body.invoke(this) {
                            //close the bottom sheet
                            scope.launch {
                                scope.launch {
                                    swipeableState.performFling(velocity = 100f)
                                }
                                swipeableState.performDrag(1000f)
                            }
                        }
                    }
                }
            }
        }
    }
}