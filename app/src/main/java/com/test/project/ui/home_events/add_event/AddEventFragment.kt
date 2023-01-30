package com.test.project.ui.home_events.add_event

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.test.project.R
import com.test.project.databinding.AddEventFragmentBinding
import com.test.project.domain.entity.Event


class AddEventFragment : Fragment(R.layout.add_event_fragment) {

    private val viewBinding: AddEventFragmentBinding by viewBinding()
    private val dataBase: DatabaseReference = FirebaseDatabase.getInstance().getReference("events")
    private val eventsRef: DatabaseReference = dataBase.child("events")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        dataBase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val id = child.key
                    val title = child.child("title").value.toString()
                    val date = child.child("date").value.toString()
                    val place = child.child("place").value.toString()
                    val description = child.child("description").value.toString()
                    val imageUrl = child.child("imageUrl").value.toString()
                    println("id: $id, title: $title, date: $date, place: $place, description: $description, imageUrl: $imageUrl")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
            var count = 0
            buttonAddEvent.setOnClickListener {

                val newEvent = Event(
                    count++,
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