package com.serhii.czechair.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhii.czechair.data.models.FlightState
import com.serhii.czechair.data.models.MapStats
import com.serhii.czechair.data.network.ApiRequestService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val api: ApiRequestService
) : ViewModel() {

    companion object {
        const val TAG = "SharedViewModel"
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        Log.d(TAG, exception.message.toString())
    }

    private val _mapStats = MutableStateFlow<MapStats?>(null)
    val mapStats: StateFlow<MapStats?> = _mapStats

    private val _selectedFlightState = MutableStateFlow<FlightState?>(null)
    val selectedFlightState: StateFlow<FlightState?> = _selectedFlightState

    //if I don't include this variable, the camera will animate even after 10 second update
    var animateSelectedFlight = false

    private var lastUpdatedTime = 0

    fun getMapStats() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = api.getCommunityList()
            if (response.isSuccessful)
                response.body()?.let {
                    if (it.time > lastUpdatedTime) {
                        lastUpdatedTime = it.time
                        val mapStats = it.toMapStats()
                        _mapStats.value = mapStats
                    }
                }
            else
                println("Error: ${response.message()}")
        }
    }

    fun setSelectedFlightState(newFlightState: FlightState?) {
        animateSelectedFlight = true
        _selectedFlightState.value = newFlightState
    }
}