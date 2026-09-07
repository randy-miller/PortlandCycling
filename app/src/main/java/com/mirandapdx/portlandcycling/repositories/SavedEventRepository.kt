package com.mirandapdx.portlandcycling.repositories

import com.mirandapdx.portlandcycling.data.Event
import kotlinx.coroutines.flow.MutableStateFlow

import androidx.room3.Dao
import androidx.room3.Database
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
}

class SavedEventRepository {
    val savedEvents = MutableStateFlow<List<Event>>(listOf())
    companion object {
        val single = SavedEventRepository()
        lateinit var db: AppDatabase
        fun setDatabase(db: AppDatabase) {
            this.db = db
            single.savedEvents.update {
                db.eventDao().get()
            }
        }

        fun update(events: List<Event>) {
            CoroutineScope(Dispatchers.IO).launch {
                db.eventDao().update(events)
            }
        }

        fun get(): SavedEventRepository {
            return single
        }
    }
}