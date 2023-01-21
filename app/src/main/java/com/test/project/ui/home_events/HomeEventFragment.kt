package com.test.project.ui.home_events

import android.os.Bundle
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.project.R
import com.test.project.data.remote.entity.FavoriteEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.fragment.app.Fragment
import com.test.project.databinding.HomeEventFragmentBinding


class HomeEventFragment : Fragment(R.layout.home_event_fragment) {

    private val viewBinding: HomeEventFragmentBinding by viewBinding()
    private val adapterEvent: HomeEventListAdapter = HomeEventListAdapter()
    private val model: HomeEventViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.eventStateFlow.collect {
                        adapterEvent.setUpdatedData(it)
                    }
                }
            }
        }
        bindUi()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model.eventFavoriteStateFlow.observe(viewLifecycleOwner) { it ->
            adapterEvent.setUpdateFavoriteList(it.map { it.id })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun bindUi() {
        with(viewBinding) {
            toolBar.inflateMenu(R.menu.home_event_menu)
            val searchView = toolBar.menu.findItem(R.id.search_event).actionView as SearchView
            searchView.queryHint = "Поиск..."
            with(recyclerViewHomeEventList) {
                adapter = adapterEvent
                layoutManager = LinearLayoutManager(requireContext())
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            model.searchEvent(newText)
                        }
                        return false
                    }
                })
                adapterEvent.setOnItemClickListener(object :
                    HomeEventListAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle()
                        bundle.putInt("position", position)
                        findNavController().navigate(
                            R.id.action_homeEventsFragment_to_eventsFragment,
                            bundle
                        )
                    }

                    override fun onAddToFavoriteButtonClick(id: Int) {
                        val idIsContains = adapterEvent.favoriteEvent.contains(id)
                        val favoriteEvent = FavoriteEvent(id)
                        if (!idIsContains) {
                            model.addToFavoriteEvent(favoriteEvent)
                        } else {
                            model.deleteFromFavoriteEvent(favoriteEvent)
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