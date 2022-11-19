package com.test.project.domain.repo

import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Friend
import com.test.project.domain.entity.ProfileMy

interface IProfileRepo {
    suspend fun getProfileMy(): RequestResult<ProfileMy>
    suspend fun getFriends(): RequestResult<List<Friend>>
    suspend fun getFriendProfileById(id: Int): RequestResult<Friend>
}
