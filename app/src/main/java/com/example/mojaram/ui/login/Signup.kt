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
// FirebaseAuth 사용하기 위해 import
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
// Log 기능을 사용하기 위한 import
import android.util.Log

class SignUpActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextid: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnSignUp: Button

    // Firebase Auth 인증 서비스 객체 생성
    private lateinit var mAhth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Firebase Auth 인증 객체 초기화
        mAhth = Firebase.auth

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

            // firebase Register
            login(userid, password)

            // 회원가입이 성공하면 로그인 화면으로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // 현재 화면을 종료
        }
    }


    // firebase login
    private fun login(userid: String, password:String){

        mAhth.signInWithEmailAndPassword(userid, password)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    // 로그인이 성공했을 때
                    val intent: Intent = Intent(this@SignUpActivity, 
                        MainActivity::class.java) // MainActivity(로그인화면)으로 이동
                    startActivity(intent) // 실행

                    Toast.makeText(this,"성공적으로 로그인 하였습니다.",
                        Toast.LENGTH_SHORT).show()
                    finish() // 성공 시 액티비티 파괴

                }else{
                    // 로그인에 실패했을 때
                    Toast.makeText(this,"로그인에 실패하였습니다.",
                        Toast.LENGTH_SHORT).show()
                    Log.d("Login Error", "Error: {${task.exception}")
                }
            }
    }

}
