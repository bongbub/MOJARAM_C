package com.example.mojaram

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.ui.login.SignUpActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Mojaram)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnM: Button = findViewById(R.id.btn_m)
        val btnS: Button = findViewById(R.id.btn_s)

        btnM.setOnClickListener {
            val intent = Intent(this, com.example.mojaram.LoginActivity::class.java)
            startActivity(intent)
        }

        btnS.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    fun onFindIdClick(view: View?) {
        val intent = Intent(this, Findid::class.java)
        startActivity(intent)
    }

    fun onFindPwdClick(view: View?) {
        val intent = Intent(this, Findqw::class.java)
        startActivity(intent)
    }
}