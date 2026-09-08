package com.mirandapdx.portlandcycling.services

import android.util.Log
import com.mirandapdx.portlandcycling.CycleApp
import com.mirandapdx.portlandcycling.data.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDate
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class FetchMode {
    TODAY
}

object Services {

    // publicly this uses kotlin's LocalDate class for the rest of the class
    val currentDates =
        MutableStateFlow<Pair<kotlinx.datetime.LocalDate?, kotlinx.datetime.LocalDate?>>(null to null)
    val eventFlow = MutableStateFlow<List<Event>>(listOf())

    private var _currentDates: Pair<LocalDate?, LocalDate?> = null to null
        set(newDates) {
            field = newDates
            currentDates.update {
                val (startDate, endDate) = newDates
                startDate?.toKotlinLocalDate() to endDate?.toKotlinLocalDate()
            }
        }


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy<ApiService> {
        Retrofit.Builder()
            .baseUrl(CycleApp.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val defaultFetch = FetchMode.TODAY

    fun updateDates(newDates: Pair<LocalDate?, LocalDate?>) {
        CoroutineScope(Dispatchers.IO).launch{
            eventFlow.update { getEventsForDate(newDates) }
        }
    }

    // be default we fetch today's events on app load
    // in the future this can be controlled via app setting
    suspend fun defaultFetch(): List<Event> =
        when (defaultFetch) {
            FetchMode.TODAY -> getEventsToday()
        }

    suspend fun getEventsToday(): List<Event> = getEventsForDate(null to null)

    suspend fun getEventsForDate(dateRange: Pair<LocalDate?, LocalDate?>): List<Event> {
        val (start, end) = dateRange
        return (start ?: LocalDate.now()).let { startDate -> // startDate default to now()
            (end ?: startDate).let { endDate -> // emdDate defaults to startDate
                _currentDates = startDate to endDate

                apiService.getEvents(
                    startdate = startDate.apiFormat(),
                    enddate = endDate.apiFormat()
                ).let { response ->
                    Log.d("ApiService", "got response ${response.isSuccessful}")

                    if (response.isSuccessful) {
                        // don't return raw API data, get the Event object
                        response.body()?.events?.map { it.model }
                            ?: listOf<Event>().also {
                                Log.e("ApiService", "no events for selected range")
                            }
                    } else {
                        Log.e("ApiService", "got response ${response.errorBody()}")
                        listOf()
                    }
                }
            }
        }
    }
}

fun LocalDate.apiFormat(): String = format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
