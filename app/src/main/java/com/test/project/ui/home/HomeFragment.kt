package com.test.project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.project.R
import com.test.project.data.remote.entity.FavoriteNews
import com.test.project.databinding.HomeFragmentBinding
import com.test.project.ui.home_events.HomeEventsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewBinding: HomeFragmentBinding by viewBinding()
    private val adapterNews: HomeNewsListAdapter = HomeNewsListAdapter()
    private val model: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.newsStateFlow.collect {
                        adapterNews.setUpdatedData(it)
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
        model.newsFavoriteStateFlow.observe(viewLifecycleOwner) { it ->
            adapterNews.setUpdateFavoriteList(it.map { it.id })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun bindUi() {
        with(viewBinding) {
            with(recyclerViewHomeList) {
                adapter = adapterNews
                layoutManager = LinearLayoutManager(requireContext())
                adapterNews.setOnItemClickListener(object :
                    HomeNewsListAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle()
                        bundle.putInt("position", position)
                        findNavController().navigate(
                            R.id.action_homeFragment_to_fullNewsFragment,
                            bundle
                        )
                    }

                    override fun onAddToFavoriteButtonClick(id: Int) {
                        val idIsContains = adapterNews.favoriteNews.contains(id)
                        val favoriteNews = FavoriteNews(id)
                        if (!idIsContains) {
                            model.addToFavoriteNews(favoriteNews)
                        } else {
                            model.deleteFromFavoriteNews(favoriteNews)
                        }
                        model.getFavoriteNewsFromDatabase()
                    }
                })
            }
            swipeRefreshHome.setOnRefreshListener {
                model.getNews()
                swipeRefreshHome.isRefreshing = false
            }
        }
    }
}