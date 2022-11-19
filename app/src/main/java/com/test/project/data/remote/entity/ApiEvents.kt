package com.test.project.data.remote.entity

import com.test.project.domain.entity.Events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiEvents(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "date")
    val date: String?,
    @Json(name = "place")
    val place: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
)

fun ApiEvents.toEvents() = Events(
    id = this.id?: -1,
    title = this.title?: "",
    date = this.date?: "",
    place = this.place?: "",
    description = this.description?: "",
    imageUrl = this.imageUrl?: ""
)

fun ApiEvents.toApiEventsDatabase() = ApiEventsDatabase(
    id = this.id?: -1,
    title = this.title?: "",
    date = this.date?: "",
    place = this.place?: "",
    description = this.description?: "",
    imageUrl = this.imageUrl?: ""
)