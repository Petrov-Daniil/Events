package com.test.project.data.dataSource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.project.data.remote.entity.ApiEventsDatabase
import com.test.project.data.remote.entity.FavoriteEvents

@Database(entities = [ApiEventsDatabase::class, FavoriteEvents::class], version = 1)
abstract class EventsDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao
    companion object {
        @Volatile
        private var INSTANCE: EventsDatabase? = null

        fun getDatabase(context: Context): EventsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    EventsDatabase::class.java, "events_hash"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}