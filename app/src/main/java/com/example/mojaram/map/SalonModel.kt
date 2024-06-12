package com.example.mojaram.map

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SalonModel(
    val shopId: Long,
    val image: String,
    val shopName: String,
    val shopKeyWord: List<String>,
    val latitude: Double?,
    val longitude: Double?,
    val address: String,
    val operationTime: String
): Parcelable