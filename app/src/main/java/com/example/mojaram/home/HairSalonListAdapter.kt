package com.example.mojaram.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mojaram.databinding.ItemSalonRecommendationBinding
import com.example.mojaram.utils.DiffCallback

class HairSalonListAdapter: ListAdapter<HairSalonListEntity, HairSalonListAdapter.HairSalonViewHolder>(
    DiffCallback<HairSalonListEntity>()
) {
    class HairSalonViewHolder(val binding: ItemSalonRecommendationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HairSalonViewHolder {
        val binding = ItemSalonRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HairSalonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HairSalonViewHolder, position: Int) {
        val data = getItem(holder.absoluteAdapterPosition)
        holder.binding.run {
            textviewName.text = data.name
            textviewLikeCount.text = data.likeCount.toString()
            checkboxLike.isChecked = data.liked
            if(data.image.isNotEmpty()) {
                imageviewSalon.load(data.image)
            }
        }
    }
}