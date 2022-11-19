package com.test.project.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.project.domain.entity.ProfileMy

@JsonClass(generateAdapter = true)
data class ApiProfileMy(
    @Json(name = "full_name")
    val fullName: String?,
    @Json(name = "group")
    val group: String?,
    @Json(name = "avatar_url")
    val avatarUrl: String?,
    @Json(name = "tel")
    val phoneNumber: String?
)

fun ApiProfileMy?.toProfileMy() = ProfileMy(
    fullName = this?.fullName ?: "",
    group = this?.group ?: "",
    avatarUrl = this?.avatarUrl ?: "",
    phoneNumber = this?.phoneNumber ?: ""
)
