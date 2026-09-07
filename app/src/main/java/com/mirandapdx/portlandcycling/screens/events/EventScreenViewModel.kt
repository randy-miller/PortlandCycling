package com.mirandapdx.portlandcycling.screens.events

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.mirandapdx.portlandcycling.data.Event
import com.mirandapdx.portlandcycling.repositories.SavedEventRepository
import com.mirandapdx.portlandcycling.screens.ScreenViewModel
import com.mirandapdx.portlandcycling.services.Services
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
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
    val toggleSave = { event: Event ->
        savedEvents.update {
            savedEvents.value.let {
                it.toMutableList().apply {
                    if (contains(event)) remove(event) else add(event)
                }
            }
        }
        SavedEventRepository.update(savedEvents.value)
    }

    val dateChanged = { newDates: Pair<LocalDate?, LocalDate?> ->
        val (newStart, newEnd) = newDates
        val (oldStart, oldEnd) = viewState.value.dateRange
        if (newStart != oldStart || newEnd != oldEnd) {
            viewState.value = viewState.value.copy(dateRange = newDates)
            Services.updateDates(newStart?.toJavaLocalDate() to newEnd?.toJavaLocalDate())
        }
    }

    val sortChanged = { newSort: SortMode ->
        viewState.value = viewState.value.copy(sortBy = newSort)
    }
}