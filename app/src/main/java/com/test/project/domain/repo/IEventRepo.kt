package com.test.project.domain.repo

import com.test.project.data.remote.entity.ApiEventDatabase
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Event
import com.test.project.data.remote.entity.FavoriteEvent

interface IEventRepo {
    suspend fun getEvents(): RequestResult<List<Event>>

    suspend fun getEventsFromDatabase(): List<Event>

    suspend fun insertToFavorite(favoriteEvent: FavoriteEvent)

    suspend fun deleteFromFavoriteById(id: Int)

    suspend fun getFavoriteEventsFromDatabase(): List<FavoriteEvent>

    suspend fun getAll(): List<ApiEventDatabase>

    suspend fun getSizeOfDatabase(): Int

    suspend fun deleteFromDatabase(id: Int)
}