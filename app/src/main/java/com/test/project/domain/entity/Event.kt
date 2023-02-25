package com.test.project.domain.entity

import com.test.project.data.remote.entity.ApiEventDatabase


data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val place: String,
    val description: String,
    val imageUrl: String,
)

fun Event.toApiEventsDatabase() = ApiEventDatabase(
    id = this.id ?: -1,
    title = this.title ?: "",
    date = this.date ?: "",
    place = this.place ?: "",
    description = this.description ?: "",
    imageUrl = this.imageUrl ?: ""
)