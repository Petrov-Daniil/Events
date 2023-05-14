package com.test.project.ui.home_events

import android.app.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.app.AlertDialog
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
import com.test.project.utils.*
import com.test.project.utils.Notification
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class HomeEventFragment : Fragment(R.layout.home_event_fragment) {

    private val viewBinding: HomeEventFragmentBinding by viewBinding()
    private val adapterEvent: HomeEventListAdapter = HomeEventListAdapter()
    private val model: HomeEventViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotificationChannel()
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        model.eventFavoriteStateFlow.observe(viewLifecycleOwner) { it ->
            adapterEvent.setUpdateFavoriteList(it.map { it.id })
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun bindUi() {
        with(viewBinding) {
            toolBar.inflateMenu(R.menu.home_event_menu)
            toolBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add_event -> {
                        findNavController().navigate(
                            R.id.action_homeEventsFragment_to_addEventFragment
                        )
                        true
                    }
                    else -> false
                }
            }
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
                            R.id.action_homeEventsFragment_to_fullEventFragment,
                            bundle
                        )
                    }

                    override fun onNotificationButtonClick(id: Int) {
                        val idIsContains = adapterEvent.favoriteEvent.contains(id)
                        val favoriteEvent = FavoriteEvent(id)
                        if (!idIsContains) {
                            val eventMessage = "Скоро начнется мероприятие, проверь приложение!"
                            eventNotification(adapterEvent.getItem(id).title, eventMessage)
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

    private fun eventNotification(eventTitle: String, eventMessage: String) {
        val intent = Intent(requireContext().applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, eventTitle)
        intent.putExtra(messageExtra, eventMessage)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        getTime { selectedDateTime ->
            val millis =
                selectedDateTime.atZone(ZoneId.systemDefault())?.toInstant()
                    ?.toEpochMilli()
            if (millis != null) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    millis,
                    pendingIntent
                )
                showAlert(selectedDateTime, eventTitle)
            }
        }
    }


    @Suppress("SameParameterValue")
    private fun showAlert(date: LocalDateTime, title: String) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val formattedDateTime = date.format(formatter)
        AlertDialog.Builder(requireContext())
            .setTitle("Напоминание")
            .setMessage("$title\nна $formattedDateTime")
            .setPositiveButton("Закрыть") { _, _ -> }
            .show()
    }

    private fun getTime(callback: (LocalDateTime) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val selectedDateTime =
                            LocalDateTime.of(selectedDate, LocalTime.of(hourOfDay, minute))
                        callback(selectedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    private fun createNotificationChannel() {
        val name = "Event Notification Channel"
        val desc = "A Description of the Channel"
        val channel = NotificationChannel(
            channelID, name,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = desc
        }
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}