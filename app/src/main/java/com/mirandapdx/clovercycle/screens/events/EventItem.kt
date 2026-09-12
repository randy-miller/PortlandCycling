package com.mirandapdx.clovercycle.screens.events

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mirandapdx.clovercycle.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.collectAsState
import com.mirandapdx.clovercycle.ui.theme.PurpleGrey40
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventItem(event: Event,
              saved: Boolean = false,
              toggleSave: (event: Event) -> Unit,
              loadAddr: (Context, String) -> (() -> Unit),
              formatTime: (Event) -> String) {
    val state = remember { MutableStateFlow(EventItemState()) }
    Box(Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .border(
            width = if(saved) 2.dp else 1.dp,
            color = if(saved) Color.Yellow else PurpleGrey40,
            shape = RoundedCornerShape(12.dp))
        .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 8.dp) // inner padding for content
        .combinedClickable(
            onLongClick = {
                toggleSave(event)
            },
            interactionSource = null,
            indication = null,
            onClick = {}
        )
    ) {
        val collectedState = state.collectAsState()
        Column {
            ItemHeader(formatTime, event, collectedState.value.expanded) {
                state.update {
                    state.value.copy(expanded = !state.value.expanded)
                }
            }
            EventHeader(event, loadAddr)
            EventBody(event, collectedState.value.expanded)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun EventBody(event: Event, expanded: Boolean = false) {
    if(expanded) {
        Column {
            HorizontalDivider()
            Text("organizer: ${event.organizer}")
            Text("details: ${event.details}")
        }
    }
}

@Composable
private fun EventHeader(
    event: Event,
    loadAddr: (Context, String) -> () -> Unit
) {
    Column {
        Text(
            text = event.title,
            fontWeight = FontWeight.ExtraBold
        )
        VenueLink(event.venue, event.address, loadAddr)
    }
}

@Composable
private fun ItemHeader(
    formatTime: (Event) -> String,
    event: Event,
    expanded: Boolean,
    expand: () -> Unit
) {
    Box {
        Text(text = formatTime(event))
        Text(
            text = if (expanded) "-" else "+",
            textAlign = TextAlign.Right,
            modifier =
                Modifier.fillMaxWidth()
                    .clickable(onClick = expand)

        )
    }
    HorizontalDivider()
}

@Composable
fun VenueLink(venue: String,
              address: String,
              loadAddr: (Context, String) -> (() -> Unit)) {
    val context = LocalContext.current
    Text(
        text = venue,
        fontWeight = FontWeight.Bold,
        color = Color.Yellow,
        modifier = Modifier.clickable(onClick = loadAddr(context, address)))

}