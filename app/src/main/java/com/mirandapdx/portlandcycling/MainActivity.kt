package com.mirandapdx.portlandcycling

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.mirandapdx.portlandcycling.data.Event
import com.mirandapdx.portlandcycling.repositories.AppDatabase
import com.mirandapdx.portlandcycling.repositories.SavedEventRepository
import com.mirandapdx.portlandcycling.screens.events.EventScreen
import com.mirandapdx.portlandcycling.screens.saved.SavedEventScreen
import com.mirandapdx.portlandcycling.services.Services
import com.mirandapdx.portlandcycling.ui.theme.PortlandCyclingTheme
import com.mirandapdx.portlandcycling.ui.theme.PurpleGrey40
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val savedEventRepository = SavedEventRepository.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val events = Services.eventFlow
        lifecycleScope.launch(Dispatchers.IO) {
            // set up saved event database
            savedEventRepository.setDatabase(
                Room.databaseBuilder<AppDatabase>(applicationContext, "pdx-event-db")
                    .setDriver(AndroidSQLiteDriver())
                    .build())
        }

        lifecycleScope.launch(Dispatchers.IO) {
            // fetch events from api
            events.update {
                Log.e("onCreate", "Fetching events")
                Services.defaultFetch()
            }
        }
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(PurpleGrey40.toArgb()))
        setContent {
            PortlandCyclingTheme {
                PortlandCyclingApp(requestedEvents = events, savedEventRepository.savedEvents)
            }
        }
    }
}

@Composable
fun PortlandCyclingApp(requestedEvents: StateFlow<List<Event>>, savedEvents: MutableStateFlow<List<Event>>) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.EVENTS) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when(currentDestination) {
                AppDestinations.EVENTS -> EventScreen(
                    modifier = Modifier.padding(innerPadding),
                    events = requestedEvents,
                    savedEvents = savedEvents)
                AppDestinations.SAVED -> SavedEventScreen(
                    modifier = Modifier.padding(innerPadding),
                    savedEvents = savedEvents)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    EVENTS("Events", R.drawable.ic_home),
    SAVED("Saved", R.drawable.ic_favorite),
}