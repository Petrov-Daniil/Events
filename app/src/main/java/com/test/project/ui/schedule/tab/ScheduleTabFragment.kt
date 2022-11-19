package com.test.project.ui.schedule.tab

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.project.R
import com.test.project.databinding.ScheduleTabFragmentBinding
import com.test.project.ui.schedule.ScheduleViewModel

class ScheduleTabFragment : Fragment(R.layout.schedule_tab_fragment) {

    private val binding: ScheduleTabFragmentBinding by viewBinding()
    private val model: ScheduleViewModel by activityViewModels()
    private val adapterScheduleTabRecyclerViewAdapter: ScheduleTabRecyclerViewAdapter =
        ScheduleTabRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewScheduleTab.adapter = adapterScheduleTabRecyclerViewAdapter
        binding.recyclerviewScheduleTab.layoutManager = LinearLayoutManager(this.context)
        arguments?.takeIf { it.containsKey("position") }?.apply {
            val position = getInt("position")
            if(model.scheduleArray[position].isEmpty()) {
                binding.textviewNoLessonLabel.text = "Нет занятий"
            }
            adapterScheduleTabRecyclerViewAdapter.setUpdatedData(model.scheduleArray[position])
        }
    }
}