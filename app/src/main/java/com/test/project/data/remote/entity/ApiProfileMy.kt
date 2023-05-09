package com.test.project.data.remote.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.project.domain.entity.ProfileMy

@JsonClass(generateAdapter = true)
data class ApiProfileMy(
    @Json(name = "role")
    val role: String?,
    @Json(name = "full_name")
    val fullName: String?,
    @Json(name = "surname")
    val surname: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "group")
    val group: String?,
    @Json(name = "tel")
    val phoneNumber: String?,
    @Json(name = "avatar_url")
    val avatarUrl: String?,
)

fun ApiProfileMy?.toProfileMy() = ProfileMy(
    role = this?.role ?: "",
    fullName = this?.fullName ?: "",
    surname = this?.surname ?: "",
    email = this?.email ?: "",
    group = this?.group ?: "",
    phoneNumber = this?.phoneNumber ?: "",
    avatarUrl = this?.avatarUrl ?: "",
)
