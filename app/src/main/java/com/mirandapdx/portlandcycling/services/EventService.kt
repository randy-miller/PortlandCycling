package com.mirandapdx.portlandcycling.services

import com.mirandapdx.portlandcycling.services.data.EventsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events.php")
    suspend fun getEvents(@Query("id") id: String = "",
                          @Query("startdate") startdate: String,
                          @Query("enddate") enddate: String,
                          @Query("all") all: Boolean = false
                          ) : Response<EventsResponse>
}