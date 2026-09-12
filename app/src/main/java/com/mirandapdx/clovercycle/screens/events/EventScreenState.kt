package com.mirandapdx.clovercycle.screens.events

import com.mirandapdx.clovercycle.screens.ScreenState
import kotlinx.datetime.LocalDate

enum class EventDialog {
    DATE, SORT, NONE;
}

enum class SortMode {
    TIME, LOCATION;

    val string: String
        get() = when(this) {
            TIME -> "Time"
            LOCATION -> "Location"
        }
}
data class EventScreenState(
    override val name: String = "Events",
    var displayedDialog: EventDialog = EventDialog.NONE,
    val sortBy: SortMode = SortMode.TIME,
    val dateRange: Pair<LocalDate?, LocalDate?> = null to null
) : ScreenState(name) {
    val datePickerDisplayed: Boolean
        get() = displayedDialog == EventDialog.DATE
    val sortDialogDisplayed: Boolean
        get() = displayedDialog == EventDialog.SORT
}