package com.test.project.ui.schedule.tab

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.project.databinding.ItemScheduleListBinding
import com.test.project.domain.entity.Lesson

class ScheduleTabRecyclerViewAdapter :
    RecyclerView.Adapter<ScheduleTabRecyclerViewAdapter.ViewHolder>() {

    private val dataList: MutableList<Lesson> = mutableListOf()
    private val timeList = listOf(
        "8:00", "9:50", "11:40", "13:45", "15:35", "17:25"
    )

    fun setUpdatedData(dataList: List<Lesson>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemScheduleListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Lesson, position: Int) {
            with(binding) {
                with(data) {
                    textviewName.text = name
                    textviewType.text = type
                    textviewTeacher.text = teacher
                    textviewTime.text = timeList.getOrNull(number - 1)
                    textviewClassroom.text = classroom
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemScheduleListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }
}