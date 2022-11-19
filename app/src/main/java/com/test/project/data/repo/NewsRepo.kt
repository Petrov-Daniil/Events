package com.test.project.data.repo

import com.test.project.data.dataSource.SibgutiHerokuRemoteDataSource
import com.test.project.data.dataSource.database.NewsDatabase
import com.test.project.data.remote.entity.FavoriteNews
import com.test.project.data.remote.entity.toApiNewsDatabase
import com.test.project.data.remote.entity.toNews
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.News
import com.test.project.domain.repo.INewsRepo

class NewsRepo(
    private val dataSource: SibgutiHerokuRemoteDataSource,
    private val database: NewsDatabase
) : INewsRepo {

    override suspend fun getNews(): RequestResult<List<News>> {
        return when (val result = dataSource.getNews()) {

            is RequestResult.Success -> {
                database.newsDao().deleteAll()
                RequestResult.Success(
                    result.data.map {
                        with(database.newsDao()) {
                            if (size() < 3)
                                insert(it.toApiNewsDatabase())
                        }
                        it.toNews()
                    }
                )
            }

            is RequestResult.Error -> {
                RequestResult.Error(
                    result.exception
                )
            }
        }
    }

    override suspend fun getNewsFromDatabase(): List<News> {
        return database.newsDao().getAll().map {
            it.toNews()
        }
    }

    override suspend fun insertToFavorite(favoriteNews: FavoriteNews) {
        database.newsDao().insertIntoFavorite(favoriteNews)
    }

    override suspend fun deleteFromFavoriteById(id: Int) {
        database.newsDao().deleteFromFavoriteById(id)
    }

    override suspend fun getFavoriteNewsFromDatabase(): List<FavoriteNews> {
        return database.newsDao().getAllFromFavorite()
    }
}
