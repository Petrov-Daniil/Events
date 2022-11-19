package com.test.project.domain.entity

import com.test.project.data.remote.entity.ApiUser

data class News(
    val dateTime: String?,
    val author: ApiUser?,
    val imageUrl: String?,
    val description: String?,
    val id: Int,
    val title: String?
)