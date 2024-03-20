package com.example.mojaram

data class User(
    var name: String,
    var email: String,
    var password: String,
    // 닉네임(이름) 받고, 생년월일 받기
    var nickname: String,
    var birth: String? = null
){
    constructor():this("","","","","")
}
