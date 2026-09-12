package com.mirandapdx.clovercycle.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey
    val id: String,
    val title: String,
    val organizer: String,
    val venue: String,
    val address: String,
    val time: String,
    val date: String,
    val details: String)