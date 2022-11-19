package com.test.project.ui.home

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.test.project.R
import com.test.project.databinding.ItemHomeNewsListBinding
import com.test.project.domain.entity.News

class HomeNewsListAdapter :
    RecyclerView.Adapter<HomeNewsListAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener
    lateinit var diff : HomeDIffUtils

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onAddToFavoriteButtonClick(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private var dataList: MutableList<News> = mutableListOf()

    var favoriteNews = mutableListOf<Int>()

    fun setUpdatedData(dataList: List<News>) {
        diff = HomeDIffUtils(this.dataList,dataList)
        val diffResult : DiffUtil.DiffResult = DiffUtil.calculateDiff(diff)
        this.dataList = dataList.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun setUpdateFavoriteList(list: List<Int>) {
        this.favoriteNews.clear()
        this.favoriteNews.addAll(list)
    }

    inner class ViewHolder(private val binding: ItemHomeNewsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: News) {
            with(binding) {
                textviewItemDescription.text = data.description
                textviewItemTitle.text = data.title
                textviewItemTitle.typeface = Typeface.DEFAULT_BOLD
                imageviewItemImage.load(data.imageUrl) {
                    crossfade(true)
                }
                textviewItemAuthor.text = data.author?.fullName ?: ""
                textviewItemDate.text = data.dateTime
                cardviewNewsCard
                    .startAnimation(
                        AnimationUtils
                            .loadAnimation(itemView.context,
                            R.anim.animation_recyclerview))

                if (favoriteNews.contains(data.id)) {
                    addToFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_selected)
                    addToFavorite.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.favorite_button_border_selected
                    )
                    addToFavoriteTextview.setTextColor(addToFavorite.context.getColor(R.color.navy_blue))
                    addToFavoriteTextview.text = "Добавлено"
                }
            }
            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
            binding.addToFavorite.setOnClickListener {
                listener.onAddToFavoriteButtonClick(data.id)
                onFavoriteButtonClick(binding, data.id)
            }
            binding.addToFavoriteButton.setOnClickListener {
                onFavoriteButtonClick(
                    binding,
                    data.id
                )
            }
        }
    }

    private fun onFavoriteButtonClick(binding: ItemHomeNewsListBinding, id: Int) {
        with(binding) {
            if (!favoriteNews.contains(id)) {
                addToFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_selected)
                addToFavorite.background = AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.favorite_button_border_selected
                )
                addToFavoriteTextview.setTextColor(addToFavorite.context.getColor(R.color.navy_blue))
                addToFavoriteTextview.text = "Добавлено"
            } else {
                addToFavoriteButton.setImageResource(R.drawable.ic_baseline_favorite_normal)
                addToFavorite.background = AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.favorite_button_border_normal
                )
                addToFavoriteTextview.setTextColor(addToFavorite.context.getColor(R.color.default_text_color))
                addToFavoriteTextview.text = "В избранное"
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeNewsListBinding.inflate(
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