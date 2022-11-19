package com.test.project.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.project.domain.entity.News

@JsonClass(generateAdapter = true)
data class ApiNews(
    @Json(name = "date_time")
    val dateTime: String?,
    @Json(name = "author")
    val author: ApiUser?,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "title")
    val title: String?
)

fun ApiNews.toNews() = News(
    dateTime = this.dateTime ?: "",
    author = this.author,
    imageUrl = this.imageUrl ?: "",
    description = this.description ?: "",
    id = this.id ?: -1,
    title = this.title ?: ""
)

fun ApiNews.toApiNewsDatabase() = ApiNewsDatabase(
    dateTime = this.dateTime ?: "",
    author = this.author?.fullName ?: "",
    imageUrl = this.imageUrl ?: "",
    description = this.description ?: "",
    id = this.id ?: -1,
    title = this.title ?: ""
)