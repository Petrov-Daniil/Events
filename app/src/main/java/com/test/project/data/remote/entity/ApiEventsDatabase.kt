package com.test.project.data.remote.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.project.domain.entity.Events


@Entity(tableName = "events_hash")
data class ApiEventsDatabase(
    @PrimaryKey
    @ColumnInfo
    val id: Int,
    @ColumnInfo
    val title: String?,
    @ColumnInfo
    val date: String?,
    @ColumnInfo
    val place: String?,
    @ColumnInfo
    val description: String?,
    @ColumnInfo
    val imageUrl: String?,
)

fun ApiEventsDatabase.toEvents() = Events(
    id = this.id,
    title = this.title ?: "",
    date = this.date ?: "",
    place = this.place ?: "",
    description = this.description ?: "",
    imageUrl = this.imageUrl ?: "",
)