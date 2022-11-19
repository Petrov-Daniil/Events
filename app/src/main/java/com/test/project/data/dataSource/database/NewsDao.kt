package com.test.project.data.dataSource.database

import androidx.room.*
import com.test.project.data.remote.entity.ApiNewsDatabase
import com.test.project.data.remote.entity.FavoriteNews

@Dao
interface NewsDao {
    @Insert
    suspend fun insertAll(list: List<ApiNewsDatabase>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apiNewsDatabase: ApiNewsDatabase)

    @Query("DELETE FROM news_hash")
    suspend fun deleteAll()

    @Query("DELETE FROM favorite_news WHERE id = :id")
    suspend fun deleteFromFavoriteById(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertIntoFavorite(favoriteNews: FavoriteNews)

    @Query("SELECT * FROM favorite_news")
    suspend fun getAllFromFavorite(): List<FavoriteNews>

    @Query("SELECT * FROM news_hash")
    suspend fun getAll(): List<ApiNewsDatabase>

    @Query("SELECT COUNT(*) FROM news_hash")
    suspend fun size(): Int
}