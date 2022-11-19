package com.test.project.data.dataSource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.project.data.remote.entity.ApiNewsDatabase
import com.test.project.data.remote.entity.FavoriteNews

@Database(entities = [ApiNewsDatabase::class, FavoriteNews::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    NewsDatabase::class.java, "news_hash"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}