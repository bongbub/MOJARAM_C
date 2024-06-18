package com.example.mojaram.model

data class Reservation(
    val date: String,
    val reservationTimes: List<String>,
    val userId: String,
    var shopId: Long = 0,
    val userName : String
) {
    constructor(): this("", emptyList(), "", 0,"")
}

//package com.example.mojaram.model
//
//data class Reservation(
//    val date: String,
//    val reservationTimes: List<String>,
//    val userId: String,
//    val shopId: Long = 0
//){
//    constructor(): this("", emptyList(), "",0)
//}
