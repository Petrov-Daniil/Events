package com.test.project.data.repo

import com.squareup.moshi.Moshi
import com.test.project.data.dataSource.SibgutiHerokuRemoteDataSource
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Events
import com.test.project.domain.repo.IEventsRepo
import com.test.project.data.remote.entity.FavoriteEvents
import com.test.project.data.dataSource.database.EventsDatabase
import com.test.project.data.remote.entity.ApiEvents
import com.test.project.data.remote.entity.toApiEventsDatabase
import com.test.project.data.remote.entity.toEvents
import com.test.project.data.remote.network.ApiErrorMessage
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.data.remote.network.convert


class EventsRepo(
    private val dataSource: SibgutiHerokuRemoteDataSource,
    private val database: EventsDatabase
) : IEventsRepo {


    override suspend fun getEvents(): RequestResult<List<Events>> {
        val json =
            "{\"id\": 1, \"title\": \"asd\",\"date\": \"02-01-2001\",\"place\": \"SibSUTIS\",\"description\": \"\",\"imageUrl\": \"\"}"
        val result = convert<ApiEvents>(json)
        println(result)
        return if (json.isNotEmpty()) RequestResult.Success(listOf(result?.toEvents() ?: Events(-1, "", "", "", "", "")))
        else RequestResult.Error(
            exception = NetworkErrors.Http.BadRequest(
                ApiErrorMessage(
                    code = 404,
                    message = ""
                )
            )
        )
//        return when (val result = dataSource.getEvents()) {
//
//            is RequestResult.Success -> {
//                database.eventsDao().deleteAll()
//                RequestResult.Success(
//                    result.data.map {
//                        with(database.eventsDao()) {
//                            if (size() < 3)
//                                insert(it.toApiEventsDatabase())
//                        }
//                        it.toEvents()
//                    }
//                )
//            }
//
//            is RequestResult.Error -> {
//                RequestResult.Error(
//                    result.exception
//                )
//            }
//        }
    }

    override suspend fun getEventsFromDatabase(): List<Events> {
        return database.eventsDao().getAll().map {
            it.toEvents()
        }
    }

    override suspend fun insertToFavorite(favoriteEvents: FavoriteEvents) {
        database.eventsDao().insertIntoFavorite(favoriteEvents)
    }

    override suspend fun deleteFromFavoriteById(id: Int) {
        database.eventsDao().deleteFromFavoriteById(id)
    }

    override suspend fun getFavoriteEventsFromDatabase(): List<FavoriteEvents> {
        return database.eventsDao().getAllFromFavorite()
    }
}
