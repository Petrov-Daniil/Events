package com.test.project.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.project.domain.entity.Friend

@JsonClass(generateAdapter = true)
data class ApiUser(
    @Json(name = "full_name")
    val fullName: String?,
    @Json(name = "avatar_url")
    val avatarUrl: String?,
    @Json(name = "tel")
    val phone: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "group")
    val group: String?
)

fun ApiUser?.toFriend() = Friend(
    fullName = this?.fullName ?: "",
    group = this?.group ?: "",
    avatarUrl = this?.avatarUrl ?: "",
    phone = this?.phone ?: "",
    id = this?.id ?: -1
)