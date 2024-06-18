package com.example.mojaram.admin

import com.example.mojaram.map.AdminModel


data class AdminListEntity (
    val reservationTime_pick : AdminModel,
    val reservation_check : String,
    val reservation_count: String
)