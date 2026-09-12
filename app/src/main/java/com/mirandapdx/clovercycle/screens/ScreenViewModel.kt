package com.mirandapdx.clovercycle.screens

import androidx.compose.runtime.mutableStateOf

abstract class ScreenViewModel<T: ScreenState>(private val state: T) {
    val viewState = mutableStateOf(state)
}