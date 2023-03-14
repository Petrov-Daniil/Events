package com.test.project.ui.home_events

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.test.project.databinding.ItemHomeEventListBinding
import com.test.project.domain.entity.Event

class HomeEventListAdapter :
    RecyclerView.Adapter<HomeEventListAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener
    lateinit var diff: HomeEventDiffUtils

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onAddToFavoriteButtonClick(id: Int)
        fun onNotificationButtonClick(flag: Boolean)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private var dataList: MutableList<Event> = mutableListOf()

    var favoriteEvent = mutableListOf<Int>()

    fun setUpdatedData(dataList: List<Event>) {
        diff = HomeEventDiffUtils(this.dataList, dataList)
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diff)
        this.dataList = dataList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun setUpdateFavoriteList(list: List<Int>) {
        this.favoriteEvent.clear()
        this.favoriteEvent.addAll(list)
    }

    inner class ViewHolder(private val binding: ItemHomeEventListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Event) {
            with(binding) {
                if (data.imageUrl != "") {
                    imageviewItemImage.load(data.imageUrl) {
                        crossfade(true)
                    }
                }
                textviewItemTitle.text = data.title
                textviewItemTitle.typeface = Typeface.DEFAULT_BOLD
                textviewItemDate.text = data.date
            }
            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
            binding.imageButtonNotification.setOnCheckedChangeListener { checkBox, isChecked ->
                checkBox.isChecked = isChecked
                checkBox.setOnClickListener {
                    listener.onNotificationButtonClick(isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeEventListBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

}