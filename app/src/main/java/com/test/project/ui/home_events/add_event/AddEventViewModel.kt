package com.test.project.ui.home_events.add_event


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.project.data.remote.entity.FavoriteEvent
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Event
import com.test.project.domain.repo.IEventRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class AddEventViewModel(
    private val eventRepo: IEventRepo
) : ViewModel() {

    private val eventList = mutableListOf<Event>()
    private val _eventState = MutableStateFlow<List<Event>?>(null)
    val eventStateFlow = _eventState.asStateFlow().filterNotNull()


    private val _error = MutableStateFlow<NetworkErrors?>(null)

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch {
            when (val result = eventRepo.getEvents()) {
                is RequestResult.Success -> {
                    eventList.clear()
                    eventList.addAll(result.data)
                    _eventState.emit(result.data)
                }

                is RequestResult.Error -> {
                    getEventsFromDatabase()
                    _error.emit(result.exception)
                }
            }
        }
    }

     fun getEventsFromDatabase() {
        viewModelScope.launch {
            eventList.clear()
            eventList.addAll(eventRepo.getEventsFromDatabase())
            _eventState.emit(eventRepo.getEventsFromDatabase())
        }
    }
}