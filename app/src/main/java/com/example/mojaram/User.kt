package com.example.mojaram

data class User(
    var email: String? = null,
    var nickname: String? = null,
    var password: String? = null,
//    var userType: String? = null,
    var userGender: String? = null,
    var userbirth : String? = null,
    var uId : String? = null


){
    constructor():this("","","","","","")
}
