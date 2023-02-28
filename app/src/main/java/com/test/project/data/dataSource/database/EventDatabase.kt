package com.test.project.data.dataSource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.project.data.remote.entity.ApiEventDatabase
import com.test.project.data.remote.entity.FavoriteEvent

@Database(entities = [ApiEventDatabase::class, FavoriteEvent::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getDatabase(context: Context): EventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    EventDatabase::class.java, "events"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}