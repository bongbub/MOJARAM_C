package com.example.mojaram.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mojaram.databinding.CheckboxInterestCategoryBinding
import com.example.mojaram.utils.DiffCallback

class CategoryListAdapter: ListAdapter<InterestCategoryEntity, CategoryListAdapter.CategoryListViewHolder>(
    DiffCallback<InterestCategoryEntity>()
) {
    class CategoryListViewHolder(val binding: CheckboxInterestCategoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val binding = CheckboxInterestCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val item = getItem(holder.absoluteAdapterPosition)
        holder.binding.checkboxCategory.run {
            text = item.category
            isChecked = item.selected
            setOnCheckedChangeListener { button, checked ->

            }
        }
    }
}