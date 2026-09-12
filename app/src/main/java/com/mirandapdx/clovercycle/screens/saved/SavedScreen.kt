package com.mirandapdx.clovercycle.screens.saved

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mirandapdx.clovercycle.data.Event
import com.mirandapdx.clovercycle.screens.events.EventScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SavedEventScreen(modifier: Modifier = Modifier, savedEvents: MutableStateFlow<List<Event>>) {
    EventScreen(modifier, savedEvents, savedEvents, "No events saved")
}