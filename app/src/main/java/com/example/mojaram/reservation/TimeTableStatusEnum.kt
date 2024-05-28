package com.example.mojaram.reservation

import androidx.annotation.DrawableRes
import com.example.mojaram.R

enum class TimeTableStatusEnum(@DrawableRes val background: Int) {
    Available(R.drawable.background_available_time),
    Disable(R.drawable.background_disable_time),
    Selected(R.drawable.background_selected_time),
    None(R.drawable.rectangle_white);
}