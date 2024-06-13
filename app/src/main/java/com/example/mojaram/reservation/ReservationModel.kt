package com.example.mojaram.reservation

import com.google.gson.annotations.SerializedName

data class ReservationModel(
    val shopId: Long,
    val userId: String,
    val date: String,
    val reservationTimes: List<String>
)
