package com.test.project.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Friend
import com.test.project.domain.entity.ProfileMy
import com.test.project.domain.repo.IProfileRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepo: IProfileRepo
) : ViewModel() {

    private val _userState = MutableStateFlow<ProfileMy?>(null)
    val userStateFlow = _userState.asStateFlow().filterNotNull()

    private val _friendListState = MutableStateFlow<List<Friend>?>(null)
    val friendsListFlow = _friendListState.asStateFlow().filterNotNull()

    private val _error = MutableStateFlow<NetworkErrors?>(null)

    init {
        getProfileMy()
        getFriends()
    }

    private fun getFriends() {
        viewModelScope.launch {
            when (val result = profileRepo.getFriends()) {
                is RequestResult.Success -> {
                    _friendListState.emit(result.data)
                }

                is RequestResult.Error -> {
                    _error.emit(result.exception)
                }
            }
        }
    }

    private fun getProfileMy() {
        viewModelScope.launch {
            when (val result = profileRepo.getProfileMy()) {

                is RequestResult.Success -> {
                    _userState.emit(result.data)
                }

                is RequestResult.Error -> {
                    _error.emit(result.exception)
                }
            }
        }
    }
}