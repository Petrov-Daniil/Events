package com.test.project.ui.home_events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.project.R
import com.test.project.data.remote.entity.FavoriteEvents
import com.test.project.databinding.HomeEventsFragmentBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.collect


class HomeEventsFragment : Fragment(R.layout.home_events_fragment) {

    private val viewBinding: HomeEventsFragmentBinding by viewBinding()
    private val adapterEvents: HomeEventsListAdapter = HomeEventsListAdapter()
    private val model: HomeEventsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.eventsStateFlow.collect {
                        adapterEvents.setUpdatedData(it)
                    }
                }
            }
        }
        viewBinding.toolBar.inflateMenu(R.menu.home_menu)
        bindUi()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model.eventsFavoriteStateFlow.observe(viewLifecycleOwner) { it ->
            adapterEvents.setUpdateFavoriteList(it.map { it.id })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun bindUi() {
        with(viewBinding) {
            with(recyclerViewHomeEventsList) {
                adapter = adapterEvents
                layoutManager = LinearLayoutManager(requireContext())
                adapterEvents.setOnItemClickListener(object :
                    HomeEventsListAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle()
                        bundle.putInt("position", position)
                        findNavController().navigate(
                            R.id.action_homeEventsFragment_to_eventsFragment,
                            bundle
                        )
                    }

                    override fun onAddToFavoriteButtonClick(id: Int) {
                        val idIsContains = adapterEvents.favoriteEvents.contains(id)
                        val favoriteEvents = FavoriteEvents(id)
                        if (!idIsContains) {
                            model.addToFavoriteEvents(favoriteEvents)
                        } else {
                            model.deleteFromFavoriteEvents(favoriteEvents)
                        }
                        model.getFavoriteEventsFromDatabase()
                    }
                })
            }
            swipeRefreshHomeEvents.setOnRefreshListener {
                model.getEvents()
                swipeRefreshHomeEvents.isRefreshing = false
            }
        }
    }
}