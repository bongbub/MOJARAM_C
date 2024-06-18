
package com.example.mojaram.reservation

import com.google.gson.annotations.SerializedName

data class ReservationModel(
    val shopId: Long = 0L,
    val userId: String = "",
    val date: String = "",
    val reservationTimes: List<String> = emptyList(),
    val userNickName: String = ""
) {
    // 기본 생성자가 자동으로 생성됩니다
}
//package com.example.mojaram.reservation
//
//import com.google.gson.annotations.SerializedName
//
//data class ReservationModel(
//    val shopId: Long,
//    val userId: String,
//    val date: String,
//    val reservationTimes: List<String>,
//    val userNickName : String
//)