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
//        https://cdn.culture.ru/images/10f5913b-b0aa-5882-b0b8-eec21ffe8114
        val json =
            "{\"id\": 1, \"title\": \"DIGITAL PR IN IT\",\"date\": \"30.11.2022 11:00 - 30.11.2022 15:00\",\"place\": \"SibSUTIS\",\"description\": \"Медиафестиваль 'DIGITAL PR IN IT'\",\"imageUrl\": \"https://soldimarketing.ru/upload/article/PR/Chto-takoe-PR-1.jpg\"}"
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
