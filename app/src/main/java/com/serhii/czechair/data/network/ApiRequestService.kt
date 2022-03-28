package com.serhii.czechair.data.network

import com.serhii.czechair.data.models.MapStatsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiRequestService {
    @GET(NetworkConstants.CZECH_REPUBLIC)
    suspend fun getCommunityList(): Response<MapStatsResponse>
}