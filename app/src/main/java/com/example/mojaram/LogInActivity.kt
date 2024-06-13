package com.example.mojaram

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.admin.AdminMain
import com.example.mojaram.data.PreferenceManager
import com.example.mojaram.ui.login.SignUpActivity
import com.example.mojaram.utils.showToast
// FirebaseAuth 사용하기 위해 import
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {
    @Inject
    lateinit var preferenceManager: PreferenceManager


    // Firebase Auth 인증 서비스 객체 생성
    private lateinit var mAhth: FirebaseAuth
    private val db = Firebase.firestore

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

        val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // 체크 되었을 때
                editor.putBoolean("autoLogin", true)
                editor.apply()
            } else {
                // 체크 해제 되었을 때
                editor.putBoolean("autoLogin", false)
                editor.apply()
            }
        }

        // checkbox에 리스너 등록하기
        check_Box.setOnCheckedChangeListener(listener)

        // 자동로그인 여부 확인하기
        if (autoLogin && mAhth.currentUser != null) {
            // 자동로그인 상태이면서, 로그인이 되는 경우 메인화면 이동
            val intent = Intent(this, MainActivity::class.java)
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
    private fun login(emailID: String, pwd: String) {
        mAhth.signInWithEmailAndPassword(emailID, pwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    preferenceManager.saveUserId(task.result.user?.uid ?: "")
                    preferenceManager.saveUserName(task.result.user?.displayName ?: "")

                    val user = mAhth.currentUser
                    user?.let {
                        checkUserType(emailID) { userType ->
                            when (userType) {
                                "admin" -> navigateToAdminMain()
                                "customer" -> navigateToMain()
                                else -> showToast("사용자 타입을 확인할 수 없습니다.")
                            }
                        }
                    }
                } else {
                    showToast("로그인에 실패하였습니다.")
                    Log.d("Login Error", "Error: ${task.exception}")
                }
            }
    }

    private fun checkUserType(emailID: String, callback: (String?) -> Unit) {
        val userTypes = listOf("user_admin", "user_customer")
        for (userType in userTypes) {
            db.collection(userType).document(emailID).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        callback(if (userType == "user_admin") "admin" else "customer")
                        return@addOnSuccessListener
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "사용자 정보 가져오기 실패", e)
                    callback(null)
                }
        }
    }

    private fun navigateToAdminMain() {
        val intent = Intent(this@LogInActivity, AdminMain::class.java)
        startActivity(intent)
        finish()
        showToast("성공적으로 로그인 하였습니다.")
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}