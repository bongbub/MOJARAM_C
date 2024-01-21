package com.example.mojaram

data class User(
    var name: String,
    var email: String,
    var password: String
){
    constructor():this("","","")
}
