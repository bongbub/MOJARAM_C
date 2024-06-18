package com.example.mojaram.home

import com.example.mojaram.map.SalonModel

data class HairSalonListEntity(
    val salonInfo: SalonModel,
    val liked: Boolean,
    val likeCount: Int
)
