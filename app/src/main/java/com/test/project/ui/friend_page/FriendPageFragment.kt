package com.test.project.ui.friend_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.test.project.R
import com.test.project.databinding.FriendPageFragmentBinding
import com.test.project.domain.entity.Friend
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FriendPageFragment: Fragment(R.layout.friend_page_fragment) {

    private val binding: FriendPageFragmentBinding by viewBinding()
    private val model: FriendPageViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            model.profileStateFlow.collect {
                bindUi(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val friendId = arguments?.getInt("friend_id") ?: 0
        model.getFriendProfile(friendId)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun bindUi(friend: Friend) {
        with(binding) {
            with(friend) {
                friendAvatarImageview.load(avatarUrl) {
                    transformations(CircleCropTransformation())
                }
                friendNameTextview.text = fullName
                friendGroupTextview.text = group

                friendProgressBar.visibility = View.GONE
                friendAvatarImageview.visibility = View.VISIBLE
                friendNameTextview.visibility = View.VISIBLE
                friendGroupTextview.visibility = View.VISIBLE
                avatarLayout.visibility = View.VISIBLE
                callButton.visibility = View.VISIBLE
                deleteFriendButton.visibility = View.VISIBLE
            }
        }
    }
}