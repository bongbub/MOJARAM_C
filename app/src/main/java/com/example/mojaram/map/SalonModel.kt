package com.example.mojaram.map

data class SalonModel(
    val shopId: Long,
    val shopName: String,
    val shopKeyWord: List<String>,
    val latitude: Double?,
    val longitude: Double?,
    val address: String,
    val operationTime: String
)