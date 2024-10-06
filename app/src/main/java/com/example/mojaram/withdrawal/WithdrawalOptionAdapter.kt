package com.example.mojaram.withdrawal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.mojaram.databinding.ListItemWithdrawalOptionBinding

class WithdrawalOptionAdapter: ListAdapter<WithdrawalOptionModel, BaseViewHolder>(COMPARATOR) {

    private lateinit var unitSelect: (optionModel: WithdrawalOptionModel) -> Unit

    fun unitSelect(unit: (optionModel: WithdrawalOptionModel) -> Unit) {
        this.unitSelect = unit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(ListItemWithdrawalOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("SimpleDateFormat", "SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.run item@{
            with(holder.binding as ListItemWithdrawalOptionBinding) {
                optionModel = this@item

                with(layout) {
                    setOnClickListener {
                        val flag = currentList[position].getIsSelected()
                        currentList.forEach { it.setIsSelected(false) }
                        currentList[position].setIsSelected(!flag)
                        unitSelect(this@item)
                    }
                }
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<WithdrawalOptionModel>() {
            override fun areItemsTheSame(oldItem: WithdrawalOptionModel, newItem: WithdrawalOptionModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: WithdrawalOptionModel, newItem: WithdrawalOptionModel) =
                oldItem == newItem
        }
    }
}