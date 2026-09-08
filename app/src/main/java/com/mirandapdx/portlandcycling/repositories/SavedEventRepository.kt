package com.mirandapdx.portlandcycling.repositories

import android.util.Log
import com.mirandapdx.portlandcycling.data.Event
import kotlinx.coroutines.flow.MutableStateFlow

import androidx.room3.Dao
import androidx.room3.Database
import androidx.room3.Delete
import androidx.room3.DeleteTable
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Room
import androidx.room3.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.text.clear

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(items: List<Event>)

    @Query("SELECT * FROM events")
    fun get(): List<Event>

    @Query("DELETE FROM events")
    fun clear()
}

class SavedEventRepository {
    private constructor()
    val savedEvents: MutableStateFlow<List<Event>> = MutableStateFlow(listOf())
    lateinit var db: AppDatabase

    fun setDatabase(db: AppDatabase) {
        this.db = db
        emitEvents()
    }
    fun emitEvents() {
        val latest = db.eventDao().get()
        savedEvents.update {
            Log.d("SavedEventRepository", "updating saved event list: ${latest.size}")
            latest
        }
    }

    fun update(events: List<Event>) {
        CoroutineScope(Dispatchers.IO).launch {
            db.eventDao().clear()
            db.eventDao().update(events)
            emitEvents()
        }
    }

    companion object {
        val single = SavedEventRepository()

        fun get(): SavedEventRepository {
            return single
        }
    }
}