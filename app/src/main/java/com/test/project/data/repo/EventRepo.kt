package com.test.project.data.repo

import com.test.project.data.dataSource.SibgutiHerokuRemoteDataSource
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Event
import com.test.project.domain.repo.IEventRepo
import com.test.project.data.remote.entity.FavoriteEvent
import com.test.project.data.dataSource.database.EventDatabase
import com.test.project.data.remote.entity.ApiEvent
import com.test.project.data.remote.entity.toApiEventsDatabase
import com.test.project.data.remote.entity.toEvents
import com.test.project.data.remote.network.ApiErrorMessage
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.data.remote.network.convert


class EventRepo(
    private val dataSource: SibgutiHerokuRemoteDataSource,
    private val database: EventDatabase
) : IEventRepo {


    override suspend fun getEvents(): RequestResult<List<Event>> {
        val json: List<String> = listOf(
            "{\"id\": 1, \"title\": \"DIGITAL PR IN IT\",\"date\": \"30.11.2022 11:00 - 15:00\",\"place\": \"SibSUTIS\",\"description\": \"Медиафестиваль 'DIGITAL PR IN IT'\",\"imageUrl\": \"https://soldimarketing.ru/upload/article/PR/Chto-takoe-PR-1.jpg\"}",
            "{\"id\": 2, \"title\": \"Спартакиада\",\"date\": \"30.11.2022 11:00 - 15:00\",\"place\": \"SibSUTIS\",\"description\": \"Спартакиада СибГУТИ, круто, весело\",\"imageUrl\": \"https://cdn.culture.ru/images/10f5913b-b0aa-5882-b0b8-eec21ffe8114\"}",
            "{\"id\": 3, \"title\": \"День варенья\",\"date\": \"1.12.2022 11:00 - 15:00\",\"place\": \"SibSUTIS\",\"description\": \"Вкусное варенье\",\"imageUrl\": \"\"}",
            "{\"id\": 4, \"title\": \"70 лет радиосвязи\",\"date\": \"12.12.2022 14:00 - 18:00\",\"place\": \"SibSUTIS\",\"description\": \"Связь рулит!\",\"imageUrl\": \"https://jrnlst.ru/sites/default/files/shutterstock_1354583030.jpg\"}"
        )
        val result = convert<ApiEvent>(json)
        return if (result.isNotEmpty()) RequestResult.Success(result.map {
            with(database.eventDao()) {
                insert(it.toApiEventsDatabase())
            }
            it.toEvents()
        })
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
//        return RequestResult<result>
    }

    override suspend fun getEventsFromDatabase(): List<Event> {
        return database.eventDao().getAll().map {
            it.toEvents()
        }
    }

    override suspend fun insertToFavorite(favoriteEvent: FavoriteEvent) {
        database.eventDao().insertIntoFavorite(favoriteEvent)
    }

    override suspend fun deleteFromFavoriteById(id: Int) {
        database.eventDao().deleteFromFavoriteById(id)
    }

    override suspend fun getFavoriteEventsFromDatabase(): List<FavoriteEvent> {
        return database.eventDao().getAllFromFavorite()
    }

    override suspend fun deleteFromDatabase(id: Int) {
        database.eventDao().deleteFromDatabase(id)
    }
}
