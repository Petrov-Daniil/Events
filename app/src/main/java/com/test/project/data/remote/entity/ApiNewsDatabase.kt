package com.test.project.data.remote.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.project.domain.entity.News

@Entity(tableName = "news_hash")
data class ApiNewsDatabase(
    @PrimaryKey
    @ColumnInfo
    val id: Int,
    @ColumnInfo
    val dateTime: String?,
    @ColumnInfo
    val author: String?,
    @ColumnInfo
    val imageUrl: String?,
    @ColumnInfo
    val description: String?,
    @ColumnInfo
    val title: String?
)

fun ApiNewsDatabase.toNews() = News(
    dateTime = this.dateTime ?: "",
    author = ApiUser(
        fullName = this.author,
        avatarUrl = null,
        id = null,
        phone = null,
        group = null
    ),
    imageUrl = this.imageUrl ?: "",
    description = this.description ?: "",
    id = this.id,
    title = this.title ?: ""
)