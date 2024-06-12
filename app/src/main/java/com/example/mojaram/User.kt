package com.example.mojaram

data class User(
    var nickname: String? = null,
    var email: String? = null,
    var password: String? = null,
    // 닉네임(이름) 받고, 생년월일 받기
    var userType: String? = null,
    var userGender: String? = null,
    var userbirth : String? = null,
    var uId : String? = null


){
    constructor():this("","","","","","","")
}
