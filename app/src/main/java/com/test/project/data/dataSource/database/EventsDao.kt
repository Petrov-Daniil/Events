package com.test.project.data.dataSource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.project.data.remote.entity.ApiEventsDatabase
import com.test.project.data.remote.entity.FavoriteEvents

@Dao
interface EventsDao {
    @Insert
    suspend fun insertAll(list: List<ApiEventsDatabase>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apiEventsDatabase: ApiEventsDatabase)

    @Query("DELETE FROM events_hash")
    suspend fun deleteAll()

    @Query("DELETE FROM favorite_events WHERE id = :id")
    suspend fun deleteFromFavoriteById(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertIntoFavorite(favoriteEvents: FavoriteEvents)

    @Query("SELECT * FROM favorite_events")
    suspend fun getAllFromFavorite(): List<FavoriteEvents>

    @Query("SELECT * FROM events_hash")
    suspend fun getAll(): List<ApiEventsDatabase>

    @Query("SELECT COUNT(*) FROM events_hash")
    suspend fun size(): Int
}