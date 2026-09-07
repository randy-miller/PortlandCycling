package com.mirandapdx.portlandcycling.screens.saved

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mirandapdx.portlandcycling.data.Event
import com.mirandapdx.portlandcycling.screens.events.EventScreen
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SavedEventScreen(modifier: Modifier = Modifier, savedEvents: MutableStateFlow<List<Event>>) {
    EventScreen(modifier, savedEvents, savedEvents)
}