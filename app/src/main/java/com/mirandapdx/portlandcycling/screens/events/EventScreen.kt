package com.mirandapdx.portlandcycling.screens.events

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mirandapdx.portlandcycling.data.Event
import com.mirandapdx.portlandcycling.screens.events.dialogs.SelectDateDialog
import com.mirandapdx.portlandcycling.screens.events.dialogs.SortDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

@Composable
fun EventScreen(modifier: Modifier, events: StateFlow<List<Event>>, savedEvents: MutableStateFlow<List<Event>>) {
    val viewModel = remember { EventScreenViewModel(events, savedEvents) }
    var viewState by viewModel.viewState

    val events = viewModel.events.collectAsStateWithLifecycle(listOf())
    val savedEvents = viewModel.savedEvents.collectAsStateWithLifecycle(listOf())
    Column(modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)) {
        EventScreenHeader(state = viewState,
            onClickDate = {
                viewState = viewState.copy(displayedDialog = EventDialog.DATE)
            },
            onClickSort = {
                viewState = viewState.copy(displayedDialog = EventDialog.SORT)
            })
        LazyColumn(modifier = modifier) {
            items(items = events.value.sortedBy {
                when(viewState.sortBy) {
                    SortMode.TIME -> it.date + it.time
                    SortMode.LOCATION -> it.venue
                }
            }) { event ->
                EventItem(
                    event,
                    savedEvents.value.any {
                        it.id == event.id
                    },
                    viewModel.toggleSave,
                    viewModel.createMapLink,
                    viewModel.formatTime)
                LaunchedEffect(savedEvents.value) {
                    // can launch a task here when a new event is saved
                }
            }
        }
    }

    val onDismissDialog = { viewState = viewState.copy(displayedDialog = EventDialog.NONE) }
    SelectDateDialog(
        state = viewState,
        onDateSelected = viewModel.dateChanged,
        onDismiss = onDismissDialog
    )

    SortDialog(
        state = viewState,
        onUpdate = viewModel.sortChanged,
        onDismiss = onDismissDialog
    )
}

@Composable
fun EventScreenHeader(state: EventScreenState,
                      onClickDate: () -> Unit,
                      onClickSort: () -> Unit) {
    val dateRange = state.dateRange
    val (startDate, endDate) = dateRange
    Column {
        Box(Modifier.fillMaxWidth()) {
            Text(text = "Dates: " + ((startDate?.prettyFormat()) ?: "Today") +
                    (endDate?.let {"-${it.prettyFormat()}"} ?: ""),

                modifier = Modifier
                    .clickable(onClick = onClickDate)
                    .align(Alignment.CenterStart),
                fontWeight = FontWeight.ExtraBold,
                textDecoration = TextDecoration.Underline
            )
            Text("Sort by: ${state.sortBy.string}",
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .clickable(onClick = onClickSort)
                    .align(Alignment.CenterEnd),
                fontWeight = FontWeight.ExtraBold,
                textDecoration = TextDecoration.Underline)
        }
        HorizontalDivider()
    }
}

fun LocalDate.prettyFormat() : String {
    return format(LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        day()
        chars(", ")
        year()
    })
}