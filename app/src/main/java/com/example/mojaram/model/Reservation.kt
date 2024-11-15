package com.example.mojaram.model

data class Reservation(
    val date: String = "",
    val reservationTimes: List<String> = emptyList(),
    val shopId: Int = 0,
    val userId: String = "",
    var nickname: String? = null,
    var userGender: String? = null,
    var userEmail: String? = null,
)