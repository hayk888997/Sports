package com.example.sports.data.remote.api

import com.example.sports.data.remote.dto.SportPerformanceDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SportsApi {

    @POST("performances")
    suspend fun savePerformance(@Body performance: SportPerformanceDto)

    @GET("performances")
    suspend fun getPerformances(): List<SportPerformanceDto>
}
