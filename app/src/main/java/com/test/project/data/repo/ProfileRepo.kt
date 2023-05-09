package com.test.project.data.repo

import android.content.Context
import com.google.firebase.database.*
import com.test.project.data.dataSource.SibgutiHerokuRemoteDataSource
import com.test.project.data.remote.network.ApiErrorMessage
import com.test.project.data.remote.network.NetworkErrors
import com.test.project.domain.RequestResult
import com.test.project.domain.entity.Friend
import com.test.project.domain.entity.ProfileMy
import com.test.project.domain.repo.IProfileRepo
import com.test.project.utils.SharedPreferences

class ProfileRepo(
    private val dataSource: SibgutiHerokuRemoteDataSource,
    private val context: Context
) :
    IProfileRepo {

    private val dataBase: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private lateinit var profile: ProfileMy

    override suspend fun getProfileMy(): RequestResult<ProfileMy> {
        dataBase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = SharedPreferences(context).restoreUserId()
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        if (child.child("email").value.toString() == email) {
                            val role = child.child("role").value.toString()
                            val name = child.child("fullName").value.toString()
                            val surname = child.child("surname").value.toString()
                            val group = child.child("group").value.toString()
                            val phone = child.child("phoneNumber").value.toString()
                            val imageUrl = child.child("avatarUrl").value.toString()
                            profile = ProfileMy(role, name, surname, email, group, phone, imageUrl)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return if (::profile.isInitialized) RequestResult.Success(profile)
        else RequestResult.Error(
            exception = NetworkErrors.Http.BadRequest(
                ApiErrorMessage(
                    code = 404,
                    message = ""
                )
            )
        )
    }

    override suspend fun getFriends(): RequestResult<List<Friend>> {
        TODO("Not yet implemented")
    }

    override suspend fun getFriendProfileById(id: Int): RequestResult<Friend> {
        TODO("Not yet implemented")
    }
}