package com.mirandapdx.clovercycle.screens.events

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.mirandapdx.clovercycle.data.Event
import com.mirandapdx.clovercycle.repositories.SavedEventRepository
import com.mirandapdx.clovercycle.screens.ScreenViewModel
import com.mirandapdx.clovercycle.services.NetworkState
import com.mirandapdx.clovercycle.services.Services
import com.mirandapdx.clovercycle.services.appDataScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.net.URLEncoder
import java.time.format.DateTimeFormatter
import java.util.Locale

const val GEO_PREFIX = "geo:0,0?q="

class EventScreenViewModel(var events: StateFlow<List<Event>>,
                           var savedEvents: MutableStateFlow<List<Event>>)
    : ScreenViewModel<EventScreenState>(state = EventScreenState()) {


    val networkStateFlow: MutableStateFlow<NetworkState>
        get() = Services.networkState
    var networkState: NetworkState = NetworkState.FETCHING

    init {
        appDataScope.launch {
            while(true) {
                networkStateFlow.collectLatest {
                    networkState = it
                }
            }
        }
    }

    fun fetchData() =
        appDataScope.launch {
            val (startDate, endDate) = viewState.value.dateRange
            Services.updateDates(startDate?.toJavaLocalDate() to endDate?.toJavaLocalDate())
        }

    fun isRefreshing(): Boolean =
        networkState == NetworkState.FETCHING


    // loads the given street address in a default maps app activity
    val createMapLink = { context: Context, address: String ->
        {
            "$GEO_PREFIX${URLEncoder.encode(address, "UTF-8")}".toUri().let { mapUri ->
                context.startActivity(Intent(Intent.ACTION_VIEW, mapUri))
            }
        }
    }

    // formats the time returned by the API for display
    val formatTime = { event: Event ->
        buildString {
            append(event.date)
            append(" @")
            append(
                DateTimeFormatter.ofPattern("HH:mm:ss", Locale.getDefault())
                    .parse(event.time).let {
                        DateTimeFormatter.ofPattern("hh:mma", Locale.getDefault())
                            .format(it)
                    })
        }
    }

    // adds or removes the selected event from the list of saved events
    val toggleSave: (Event) -> Unit = { event: Event ->
        savedEvents.value.let {
            SavedEventRepository.get().update(it.toMutableList().apply {
                if (contains(event)) remove(event) else add(event)
            })
        }
    }

    val dateChanged = { newDates: Pair<LocalDate?, LocalDate?> ->
        val (newStart, newEnd) = newDates
        val (oldStart, oldEnd) = viewState.value.dateRange
        if (newStart != oldStart || newEnd != oldEnd) {
            viewState.value = viewState.value.copy(dateRange = newDates)
            fetchData()
        }
    }

    val sortChanged = { newSort: SortMode ->
        viewState.value = viewState.value.copy(sortBy = newSort)
    }
}