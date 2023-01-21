package com.test.project.data.remote.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.project.domain.entity.Event


@Entity(tableName = "events_hash")
data class ApiEventDatabase(
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

fun ApiEventDatabase.toEvents() = Event(
    id = this.id,
    title = this.title ?: "",
    date = this.date ?: "",
    place = this.place ?: "",
    description = this.description ?: "",
    imageUrl = this.imageUrl ?: "",
)