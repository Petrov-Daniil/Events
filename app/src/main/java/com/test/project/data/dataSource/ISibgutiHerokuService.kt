package com.test.project.data.dataSource

import com.test.project.data.remote.entity.*
import com.test.project.data.remote.network.INetwork
import retrofit2.http.GET
import retrofit2.http.Path


fun provideSibgutiHerokuService(network: INetwork): ISibgutiHerokuService =
    network.retrofit.create(
        ISibgutiHerokuService::class.java
    )


interface ISibgutiHerokuService {
    @GET("profile/my")
    suspend fun getProfileMy(): ApiProfileMy

    @GET("friends/my")
    suspend fun getFriends(): List<ApiUser>

    @GET("news/last")
    suspend fun getNews(): List<ApiNews>

    @GET("schedule")
    suspend fun getSchedule(): List<List<ApiLesson>>

    @GET("events/last")
    suspend fun getEvents(): List<ApiEvents>

    @GET("profile/{id}")
    suspend fun getProfileById(@Path("id") id: Int): ApiUser
}