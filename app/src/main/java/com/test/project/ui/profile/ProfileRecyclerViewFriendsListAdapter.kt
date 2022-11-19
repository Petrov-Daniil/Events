package com.test.project.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.test.project.R
import com.test.project.databinding.ItemProfileFriendsListBinding
import com.test.project.domain.entity.Friend

class ProfileRecyclerViewFriendsListAdapter :
    RecyclerView.Adapter<ProfileRecyclerViewFriendsListAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private var dataList: MutableList<Friend> = mutableListOf()

    fun setUpdatedData(dataList: List<Friend>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProfileFriendsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Friend) {
            with(binding) {
                textviewItemGroup.text = data.group
                textviewItemName.text = data.fullName
                textviewItemPhone.text = data.phone
                cardviewFriendsCard
                    .startAnimation(
                        AnimationUtils.loadAnimation(
                            itemView.context,
                            R.anim.animation_recyclerview))
                imageviewItemAvatar.load(data.avatarUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
            itemView.setOnClickListener {
                listener.onItemClick(data.id)
            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemProfileFriendsListBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

}