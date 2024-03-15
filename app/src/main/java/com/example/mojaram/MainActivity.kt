package com.example.mojaram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.session.MediaSessionManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.KeyEventDispatcher.Component
import com.example.mojaram.ui.login.SignUpActivity
// FirebaseAuth 사용하기 위해 import
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser



class MainActivity : AppCompatActivity() {


    // Firebase Auth 인증 서비스 객체 생성
    private lateinit var mAhth: FirebaseAuth

    private lateinit var id: EditText
    private lateinit var pwd: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Mojaram)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase Auth 인증 객체 초기화
        mAhth = Firebase.auth

        id = findViewById(R.id.id)
        pwd = findViewById(R.id.editTextTextPassword)
        val btnM: Button = findViewById(R.id.btn_my1)
        val btnS: Button = findViewById(R.id.btn_s)
        // activity_login.xml 파일에 있는 checkbox
        val check_Box: CheckBox = findViewById(R.id.checkBox)



        // SharedPreferences를 사용해서 로그인 상태 저장하기
        val sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 자동 로그인 상태 가져오기
        val autoLogin = sharedPreferences.getBoolean("autoLogin", false)
        check_Box.isChecked = autoLogin

        val listener = CompoundButton.OnCheckedChangeListener{buttonView, isChecked ->
            if(isChecked){
                // 체크 되었을 때
                editor.putBoolean("autoLogin", true)
                editor.apply()
            }
            else{
                // 체크 해제 되었을 때
                editor.putBoolean("autoLogin", false)
                editor.apply()
            }
        }

        // checkbox에 리스너 등록하기
        check_Box.setOnCheckedChangeListener(listener)

        // 자동로그인 여부 확인하기
        if(autoLogin && mAhth.currentUser != null){
            // 자동로그인 상태이면서, 로그인이 되는 경우 메인화면 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // FragmentManager등록?


        btnM.setOnClickListener {

            // text로 변환
            var emailID = id.text.toString()
            var passwd = pwd.text.toString()

            // firebase Register
            login(emailID, passwd)
        }

        btnS.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // firebase login
    fun login(emailID: String, pwd:String){

        mAhth.signInWithEmailAndPassword(emailID, pwd)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    // 로그인이 성공했을 때
                    val intent: Intent = Intent(this@MainActivity,
                        LoginActivity::class.java) // LoginActivity(로그인화면)으로 이동
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
    fun onFindIdClick(view: View?) {
        val intent = Intent(this, Findid::class.java)
        startActivity(intent)
    }

    fun onFindPwdClick(view: View?) {
        val intent = Intent(this, Findqw::class.java)
        startActivity(intent)
    }




}