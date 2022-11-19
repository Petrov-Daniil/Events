package com.test.project.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.test.project.ui.schedule.tab.ScheduleTabFragment

class ScheduleViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        val fragment = ScheduleTabFragment()
        fragment.arguments = Bundle().apply {
            putInt("position", position)
        }
        return fragment
    }
}