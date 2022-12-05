package com.test.project.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.project.data.remote.entity.FavoriteNews
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.News
import com.test.project.domain.repo.INewsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class HomeViewModel(
    private val newsRepo: INewsRepo,
) : ViewModel() {
    private val newsList = mutableListOf<News>()
    private val _newsState = MutableStateFlow<List<News>?>(null)
    val newsStateFlow = _newsState.asStateFlow().filterNotNull()

    private var _newsFavoriteState = MutableLiveData<List<FavoriteNews>>()
    val newsFavoriteStateFlow = _newsFavoriteState

    private val _error = MutableStateFlow<NetworkErrors?>(null)

    init {
        getNews()
        getFavoriteNewsFromDatabase()
    }

    fun getNews() {
        viewModelScope.launch {
            when (val result = newsRepo.getNews()) {
                is RequestResult.Success -> {
                    newsList.clear()
                    newsList.addAll(result.data)
                    _newsState.emit(result.data)
                }
                is RequestResult.Error -> {
                    getNewsFromDatabase()
                    _error.emit(result.exception)
                }
            }
        }
    }

    fun getFavoriteNewsFromDatabase() {
        viewModelScope.launch {
            _newsFavoriteState.value = newsRepo.getFavoriteNewsFromDatabase()
        }
    }

    private fun getNewsFromDatabase() {
        viewModelScope.launch {
            newsList.clear()
            newsList.addAll(newsRepo.getNewsFromDatabase())
            _newsState.emit(newsRepo.getNewsFromDatabase())
        }
    }

    fun addToFavoriteNews(favoriteNews: FavoriteNews) {
        viewModelScope.launch {
            newsRepo.insertToFavorite(favoriteNews)
        }
    }

    fun deleteFromFavoriteNews(favoriteNews: FavoriteNews) {
        viewModelScope.launch {
            newsRepo.deleteFromFavoriteById(favoriteNews.id)
        }
    }

    fun searchNews(search: String) {
        _newsState.value = newsList.filter { it.title?.contains(search, true) ?: false }
    }
}