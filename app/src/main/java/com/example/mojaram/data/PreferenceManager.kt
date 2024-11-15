package com.example.mojaram.data

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    val editor = sharedPreferences.edit();

    fun saveUserName(name: String) {
        editor.putString("USER_NAME", name).apply()
    }

    fun saveUserId(id: String) {
        editor.putString("USER_ID", id).apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString("USER_NAME", "") ?: ""
    }

    fun getUserId(): String {
        return sharedPreferences.getString("USER_ID", "") ?: ""
    }
    fun getNickname(): String? {
        return sharedPreferences.getString("nickname", null) // nickname을 가져오는 메서드
    }

    fun getUserGender(): String? {
        return sharedPreferences.getString("userGender", null) // userGender를 가져오는 메서드
    }
}