package com.example.mojaram

data class User(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    // 닉네임(이름) 받고, 생년월일 받기
    var nickname: String? = null,
    var birth: String? = null

){
    constructor():this("","","","","")
}
