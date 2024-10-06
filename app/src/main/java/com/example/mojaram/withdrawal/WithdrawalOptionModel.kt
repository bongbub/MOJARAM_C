package com.example.mojaram.withdrawal

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.mojaram.BR

data class WithdrawalOptionModel(
    var optionText: String,
    var isSelected: Boolean,
): BaseObservable() {

    @Bindable
    fun getIsSelected() = isSelected
    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
        notifyPropertyChanged(BR.isSelected)
    }
}