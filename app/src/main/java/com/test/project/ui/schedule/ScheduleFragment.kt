package com.test.project.ui.schedule

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.test.project.R
import com.test.project.databinding.ScheduleFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleFragment : Fragment(R.layout.schedule_fragment) {

    private val model: ScheduleViewModel by activityViewModels()
    private val binding: ScheduleFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerSchedule.adapter =
            ScheduleViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayoutSchedule, binding.viewPagerSchedule) { tab, position ->
            tab.text = model.tabTitles[position]
        }.attach()
    }
}