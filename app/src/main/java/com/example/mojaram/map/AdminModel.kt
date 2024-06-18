package com.example.mojaram.map

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdminModel(
    val reservation_date : String,
    val reservation_timelist : List<String>,
    val reservation_name : String
) : Parcelable
