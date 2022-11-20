package com.test.project.ui.home_events.events

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.test.project.R
import com.test.project.databinding.EventsFragmentBinding
import com.test.project.domain.entity.Events
import com.test.project.ui.home_events.HomeEventsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collect

class EventsFragment : Fragment(R.layout.events_fragment) {

    private val binding: EventsFragmentBinding by viewBinding()
    private val model: HomeEventsViewModel by viewModel()
    private val adapterEventsList: EventsListAdapter = EventsListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventsIndex = arguments?.getInt("position") ?: 0
        viewLifecycleOwner.lifecycleScope.launch {
            model.eventsStateFlow.collect {
                bindUi(it[eventsIndex])
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                launch {
                    model.eventsStateFlow.collect {
                        adapterEventsList.setUpdatedData(it)
                    }
                }
            }
        }
    }

    private fun bindUi(events: Events) {
        with(binding) {
            with(events) {
                textviewItemTitle.text = title
                textviewItemTitle.typeface = android.graphics.Typeface.DEFAULT_BOLD
                textviewItemDate.text = date
                textviewItemDescription.text = description
                textviewItemDescription.movementMethod = ScrollingMovementMethod()
                imageviewItemImage.load(imageUrl)
            }
        }
    }
}