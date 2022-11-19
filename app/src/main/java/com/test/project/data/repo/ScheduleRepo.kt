package com.test.project.data.repo

import com.test.project.data.dataSource.SibgutiHerokuRemoteDataSource
import com.test.project.data.remote.entity.toLesson
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Lesson
import com.test.project.domain.repo.IScheduleRepo

class ScheduleRepo(private val dataSource: SibgutiHerokuRemoteDataSource) : IScheduleRepo {
    override suspend fun getLessons(): RequestResult<List<List<Lesson>>> {
        return when(val result = dataSource.getSchedule()) {
            is RequestResult.Success ->
                RequestResult.Success(result.data.map { list ->
                    list.map {
                        it.toLesson()
                    }
                })
            is RequestResult.Error ->
                RequestResult.Error(result.exception)
        }
    }
}