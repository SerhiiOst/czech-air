package com.serhii.czechair.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Colors.flightListColor: Color
    @Composable
    get() = if(isLight) Color.White else Color.Black