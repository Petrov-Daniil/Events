package com.test.project.ui.home_events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.project.data.remote.entity.FavoriteEvents
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Events
import com.test.project.domain.repo.IEventsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class HomeEventsViewModel(
    private val eventsRepo: IEventsRepo
) : ViewModel() {

    private val _eventsState = MutableStateFlow<List<Events>?>(null)
    val eventsStateFlow = _eventsState.asStateFlow().filterNotNull()

    private var _eventsFavoriteState = MutableLiveData<List<FavoriteEvents>>()
    val eventsFavoriteStateFlow = _eventsFavoriteState

    private val _error = MutableStateFlow<NetworkErrors?>(null)

    init {
        getEvents()
        getFavoriteEventsFromDatabase()
    }

    fun getEvents() {
        viewModelScope.launch {
            when (val result = eventsRepo.getEvents()) {
                is RequestResult.Success -> {
                    _eventsState.emit(result.data)
                }

                is RequestResult.Error -> {
                    getEventsFromDatabase()
                    _error.emit(result.exception)
                }
            }
        }
    }

    fun getFavoriteEventsFromDatabase() {
        viewModelScope.launch {
            _eventsFavoriteState.value = eventsRepo.getFavoriteEventsFromDatabase()
        }
    }

    private fun getEventsFromDatabase() {
        viewModelScope.launch {
            _eventsState.emit(eventsRepo.getEventsFromDatabase())
        }
    }

    fun addToFavoriteEvents(favoriteEvents: FavoriteEvents) {
        viewModelScope.launch {
            eventsRepo.insertToFavorite(favoriteEvents)
        }
    }

    fun deleteFromFavoriteEvents(favoriteEvents: FavoriteEvents) {
        viewModelScope.launch {
            eventsRepo.deleteFromFavoriteById(favoriteEvents.id)
        }
    }
}