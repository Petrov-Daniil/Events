package com.test.project.domain.repo

import com.test.project.data.remote.entity.FavoriteNews
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.News

interface INewsRepo {
    suspend fun getNews(): RequestResult<List<News>>
    suspend fun getNewsFromDatabase(): List<News>
    suspend fun insertToFavorite(favoriteNews: FavoriteNews)
    suspend fun deleteFromFavoriteById(id: Int)
    suspend fun getFavoriteNewsFromDatabase(): List<FavoriteNews>
}