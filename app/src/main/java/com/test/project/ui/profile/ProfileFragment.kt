package com.test.project.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.test.project.R
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.test.project.databinding.ProfileFragmentBinding

import com.test.project.domain.entity.ProfileMy
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val viewBinding: ProfileFragmentBinding by viewBinding()
    private val model: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.userStateFlow.collect {
                        showInfo(it)
                    }
                }
                launch {
                    model.friendsListFlow.collect {
                    }
                }
            }
        }
        bindUi(view.context)
    }

    @SuppressLint("SetTextI18n")
    private fun showInfo(user: ProfileMy) {
        with(viewBinding) {
            with(user) {
                imageviewProfileAvatar.load(avatarUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                textviewProfileFullName.text = "$fullName $surname"
                textviewProfilePhone.text = phoneNumber
                textviewProfileGroup.text = group
                textviewProfileEmail.text = email
            }
        }
    }

    private fun bindUi(context: Context) {
        with(viewBinding) {
            toolBar.inflateMenu(R.menu.profile_menu)
            toolBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add_event -> {
                        findNavController().navigate(
                            R.id.action_profileFragment_to_addProfileFragment
                        )
                        true
                    }
                    else -> false
                }
            }
        }
    }

}