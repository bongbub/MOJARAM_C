package com.example.mojaram.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.R
import com.example.mojaram.MainActivity
import com.example.mojaram.LoginActivity


class SignUpActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextid: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnSignUp: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextid = findViewById(R.id.editTextid)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            // 회원가입 버튼을 클릭했을 때의 동작을 구현하세요.
            val username = editTextUsername.text.toString()
            val userid = editTextid.text.toString()
            val password = editTextPassword.text.toString()

            // 여기에 실제 회원가입 로직을 추가하세요.
            // 예를 들어, 서버에 데이터를 전송하거나 로컬 DB에 저장하는 코드를 작성할 수 있습니다.
            // 실제 로직은 서버 통신이나 데이터베이스 저장 등으로 구현해야 합니다.


            // 회원가입이 성공하면 로그인 화면으로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // 현재 화면을 종료
        }
    }

}
