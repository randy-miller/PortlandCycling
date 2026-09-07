package com.mirandapdx.portlandcycling.screens

import androidx.compose.runtime.mutableStateOf

abstract class ScreenViewModel<T: ScreenState>(private val state: T) {
    val viewState = mutableStateOf(state)
}