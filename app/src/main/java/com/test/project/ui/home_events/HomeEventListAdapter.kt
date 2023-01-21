package com.test.project.ui.home_events

import android.graphics.Typeface
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

//                if (favoriteEvents.contains(data.id)) {
//                    addToFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_selected)
//                    addToFavorite.background = AppCompatResources.getDrawable(
//                        binding.root.context,
//                        R.drawable.favorite_button_border_selected
//                    )
//                    addToFavoriteTextview.setTextColor(addToFavorite.context.getColor(R.color.navy_blue))
//                    addToFavoriteTextview.text = "Добавлено"
//                }
            }
            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
//            binding.addToFavorite.setOnClickListener {
//                listener.onAddToFavoriteButtonClick(data.id)
//                onFavoriteButtonClick(binding, data.id)
//            }
//            binding.addToFavoriteButton.setOnClickListener {
//                onFavoriteButtonClick(
//                    binding,
//                    data.id
//                )
//            }
        }
    }

//    private fun onFavoriteButtonClick(binding: ItemHomeEventsListBinding, id: Int) {
//        with(binding) {
//            if (!favoriteEvents.contains(id)) {
//                addToFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_selected)
//                addToFavorite.background = AppCompatResources.getDrawable(
//                    binding.root.context,
//                    R.drawable.favorite_button_border_selected
//                )
//                addToFavoriteTextview.setTextColor(addToFavorite.context.getColor(R.color.navy_blue))
//                addToFavoriteTextview.text = "Добавлено"
//            } else {
//                addToFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_normal)
//                addToFavorite.background = AppCompatResources.getDrawable(
//                    binding.root.context,
//                    R.drawable.favorite_button_border_normal
//                )
//                addToFavoriteTextview.setTextColor(addToFavorite.context.getColor(R.color.default_text_color))
//                addToFavoriteTextview.text = "В избранное"
//            }
//        }
//    }

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