package com.test.project.data.remote.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events", indices = [Index(value = ["id"], unique = true)])
data class FavoriteEvent(
    @PrimaryKey
    @ColumnInfo
    val id: Int
)