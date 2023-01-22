package com.test.project.ui.home_events.full_event

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.project.databinding.ItemEventListBinding
import com.test.project.domain.entity.Event

class FullEventListAdapter :
    RecyclerView.Adapter<FullEventListAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private var dataList: MutableList<Event> = mutableListOf()

    fun setUpdatedData(dataList: List<Event>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemEventListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Event) {
            with(binding) {
                textviewItemTitle.text = data.title
                textviewItemTitle.typeface = Typeface.DEFAULT_BOLD
                textviewItemDate.text = data.date
            }
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEventListBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size
}