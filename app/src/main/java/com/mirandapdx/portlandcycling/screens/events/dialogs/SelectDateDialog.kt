package com.mirandapdx.portlandcycling.screens.events.dialogs

import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import com.mirandapdx.portlandcycling.screens.events.EventScreenState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDateDialog(state: EventScreenState,
                     onDateSelected: (Pair<LocalDate?, LocalDate?>) -> Unit,
                     onDismiss: () -> Unit) {
    val dateRangePickerState = rememberDateRangePickerState()

    if(state.datePickerDisplayed)
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(
                        dateRangePickerState.getSelectedStartDate()?.toKotlinLocalDate()
                            to
                        dateRangePickerState.getSelectedEndDate()?.toKotlinLocalDate())
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(state = dateRangePickerState)
        }
}