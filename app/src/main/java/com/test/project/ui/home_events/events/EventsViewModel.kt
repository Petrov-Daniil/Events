package com.test.project.ui.home_events.events
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Events
import com.test.project.domain.repo.IEventsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class EventsViewModel(
    private val eventsRepo: IEventsRepo
) : ViewModel() {

    private val _eventsState = MutableStateFlow<List<Events>?>(null)
    val v: Int? = null
    val eventsStateFlow = _eventsState.asStateFlow().filterNotNull()

    private val _error = MutableStateFlow<NetworkErrors?>(null)

    init {
        getEvents()
    }

    fun getEvents() {
        viewModelScope.launch {
            when (val result = eventsRepo.getEvents()) {
                is RequestResult.Success -> {
                    _eventsState.emit(result.data)
                }
                is RequestResult.Error -> {
                    _error.emit(result.exception)
                }
            }
        }
    }
}