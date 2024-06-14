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
}