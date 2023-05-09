package com.test.project.ui.profile.add_profile

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.test.project.R
import com.test.project.databinding.AddProfileFragmentBinding
import com.test.project.domain.entity.ProfileMy

class AddProfileFragment : Fragment(R.layout.add_profile_fragment) {
    private val viewBinding: AddProfileFragmentBinding by viewBinding()
    private val dataBase: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        with(viewBinding) {
            toolBar.inflateMenu(R.menu.add_profile_menu)
            toolBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            val role = textInputEdittextRole.text
            val name = textInputEdittextName.text
            val surname = textInputEdittextSurname.text
            val email = textInputEdittextEmail.text
            val group = textInputEdittextGroup.text
            val phone = textInputEdittextPhoneNumber.text
            val imageUrl = textInputEdittextImage.text
            buttonAddProfile.setOnClickListener {
                val newProfile = ProfileMy(
                    role.toString(),
                    name.toString(),
                    surname.toString(),
                    email.toString(),
                    group.toString(),
                    phone.toString(),
                    imageUrl.toString(),
                )
                dataBase.push().setValue(newProfile)
                findNavController().navigateUp()
                Toast.makeText(
                    requireContext(),
                    "Новый профиль создан",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}