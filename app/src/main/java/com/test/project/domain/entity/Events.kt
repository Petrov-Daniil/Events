package com.test.project.domain.entity


data class Events(
    val id: Int,
    val title: String,
    val date: String,
    val place: String,
    val description: String,
    val imageUrl: String,
)