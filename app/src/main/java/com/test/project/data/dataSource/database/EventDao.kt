package com.test.project.data.dataSource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.project.data.remote.entity.ApiEventDatabase
import com.test.project.data.remote.entity.FavoriteEvent

@Dao
interface EventDao {
    @Insert
    suspend fun insertAll(list: List<ApiEventDatabase>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apiEventDatabase: ApiEventDatabase)

    @Query("DELETE FROM events")
    suspend fun deleteAll()

    @Query("DELETE FROM favorite_events WHERE id = :id")
    suspend fun deleteFromFavoriteById(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoFavorite(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM favorite_events")
    suspend fun getAllFromFavorite(): List<FavoriteEvent>

    @Query("SELECT * FROM events")
    suspend fun getAll(): List<ApiEventDatabase>

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getSizeOfDatabase(): Int

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteFromDatabase(id: Int)
}