package com.test.project.ui.home_events.add_event

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.test.project.R
import com.test.project.data.dataSource.database.EventDatabase
import com.test.project.databinding.AddEventFragmentBinding
import com.test.project.domain.entity.Event
import com.test.project.domain.repo.IEventRepo
import com.test.project.ui.home_events.HomeEventViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddEventFragment : Fragment(R.layout.add_event_fragment) {

    private val viewBinding: AddEventFragmentBinding by viewBinding()
    private val dataBase: DatabaseReference = FirebaseDatabase.getInstance().getReference("events")
    private var count: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi()
    }

    private fun bindUi() {
        dataBase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    count = snapshot.childrenCount.toInt()
                }
            }

            override fun onCancelled(error: DatabaseError) {
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
            buttonAddEvent.setOnClickListener {
                val newEvent = Event(
                    count + 1,
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
                ).show()
            }
        }
    }
}