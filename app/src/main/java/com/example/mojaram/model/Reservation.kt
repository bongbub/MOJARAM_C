package com.example.mojaram.model

data class Reservation(
    val date: String,
    val reservationTimes: List<String>,
    val userId: String,
    var shopId: String = ""
) {
    constructor() : this("", emptyList(), "", "")
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
