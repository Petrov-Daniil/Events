package com.test.project.ui.home_events.add_event

import com.test.project.domain.entity.Event
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.test.project.R
import com.test.project.databinding.AddEventFragmentBinding

class AddEventFragment : Fragment(R.layout.add_event_fragment) {

    private val viewBinding: AddEventFragmentBinding by viewBinding()
    private val dataBase: DatabaseReference = FirebaseDatabase.getInstance().getReference("Events")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        with(viewBinding) {
            toolBar.inflateMenu(R.menu.add_event_menu)
            toolBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            val title = textInputEdittextTitle.text
            val date = textInputEdittextDate.text
            val place = textInputEdittextPlace.text
            val description = textInputEdittextDescription.text
            val imageUrl = textInputEdittextImage.text
            buttonAddEvent.setOnClickListener {
                val id = dataBase.get()
                println(id)
                val newEvent = Event(
                    id,
                    title.toString(),
                    date.toString(),
                    place.toString(),
                    description.toString(),
                    imageUrl.toString()
                )
                dataBase.push().setValue(newEvent)
                Toast.makeText(
                    requireContext(),
                    "Мероприятие добавлено",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}