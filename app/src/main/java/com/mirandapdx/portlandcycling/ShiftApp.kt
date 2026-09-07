package com.mirandapdx.portlandcycling

import android.app.Application
import android.content.Context
class ShiftApp: Application() {

    companion object {
        const val API_URL = "https://www.shift2bikes.org/api/"
    }
}

// Optional: Provide a clean global extension property to access this class anywhere
val Context.app: ShiftApp
    get() = applicationContext as ShiftApp
