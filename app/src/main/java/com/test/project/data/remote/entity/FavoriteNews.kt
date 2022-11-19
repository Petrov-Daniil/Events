package com.test.project.data.remote.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_news", indices = [Index(value = ["id"], unique = true)])
data class FavoriteNews(
    @PrimaryKey
    @ColumnInfo
    val id: Int
)
