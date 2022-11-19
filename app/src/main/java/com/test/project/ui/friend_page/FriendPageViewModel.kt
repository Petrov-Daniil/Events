package com.test.project.ui.friend_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Friend
import com.test.project.domain.repo.IProfileRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class FriendPageViewModel(
    private val profileRepo: IProfileRepo
): ViewModel() {

    private val _profileState = MutableStateFlow<Friend?>(null)
    val profileStateFlow = _profileState.asStateFlow().filterNotNull()

    private val _error = MutableStateFlow<NetworkErrors?>(null)

    fun getFriendProfile(id: Int) {
        viewModelScope.launch {
            when (val result = profileRepo.getFriendProfileById(id)) {
                is RequestResult.Success -> {
                    _profileState.emit(result.data)
                }
                is RequestResult.Error -> {
                    _error.emit(result.exception)
                }
            }
        }
    }
}