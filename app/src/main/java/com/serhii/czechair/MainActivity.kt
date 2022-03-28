package com.serhii.czechair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.google.android.gms.maps.MapsInitializer
import com.serhii.czechair.ui.screens.map.MapScreen
import com.serhii.czechair.ui.theme.CzechFlightsTheme
import com.serhii.czechair.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    @OptIn(
        ExperimentalFoundationApi::class,
        ExperimentalMaterialApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(applicationContext)

        setContent {
            CzechFlightsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MapScreen(sharedViewModel)
                }
            }
        }

        //I decided just to launch a coroutine for 10 second update
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                CoroutineScope(Dispatchers.Main).launch {
                    sharedViewModel.getMapStats()
                }
                delay(10*1000)
            }
        }
    }
}