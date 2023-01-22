package com.test.project.ui.home_events.full_event

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.test.project.R
import com.test.project.databinding.FullEventFragmentBinding
import com.test.project.domain.entity.Event
import com.test.project.ui.home_events.HomeEventViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FullEventFragment : Fragment(R.layout.full_event_fragment) {

    private val binding: FullEventFragmentBinding by viewBinding()
    private val model: HomeEventViewModel by viewModel()
    private val adapterEventList: FullEventListAdapter = FullEventListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventIndex = arguments?.getInt("position") ?: 0
        viewLifecycleOwner.lifecycleScope.launch {
            model.eventStateFlow.collect {
                bindUi(it[eventIndex])
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                launch {
                    model.eventStateFlow.collect {
                        adapterEventList.setUpdatedData(it)
                    }
                }
            }
        }
    }

    private fun bindUi(event: Event) {
        with(binding) {
            with(event) {
                toolBar.inflateMenu(R.menu.full_event_menu)
                toolBar.setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
                toolBar.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete_event -> {
                            AlertDialog.Builder(requireContext())
                                .setMessage("Удалить мероприятие?")
                                .setPositiveButton("Удалить") { _, _ ->
                                    model.delete(event.id)
                                    Toast.makeText(
                                        requireContext(),
                                        "Мероприятие удалено",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                .setNegativeButton("Отменить", null)
                                .create()
                                .show()
                            true
                        }
                        else -> false
                    }
                }
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