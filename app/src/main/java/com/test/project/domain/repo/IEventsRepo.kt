package com.test.project.domain.repo

import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Events
import com.test.project.data.remote.entity.FavoriteEvents

interface IEventsRepo {
    suspend fun getEvents(): RequestResult<List<Events>>

    suspend fun getEventsFromDatabase(): List<Events>

    suspend fun insertToFavorite(favoriteEvents: FavoriteEvents)

    suspend fun deleteFromFavoriteById(id: Int)

    suspend fun getFavoriteEventsFromDatabase(): List<FavoriteEvents>

}