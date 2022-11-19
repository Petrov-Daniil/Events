package com.test.project.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.test.project.domain.entity.News

class HomeDIffUtils(
    private val oldList : List<News>,
    private val newList : List<News>
) : DiffUtil.Callback()
{
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}