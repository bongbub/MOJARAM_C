package com.example.mojaram.ui.login

import android.app.DatePickerDialog
import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.R
import com.example.mojaram.MainActivity
import com.example.mojaram.LoginActivity
import com.example.mojaram.User
import com.google.android.gms.common.internal.Objects.ToStringHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
// Firebase 연동을 위한 import
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import java.time.Year
import java.util.Calendar


class SignUpActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextid: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnSignUp: Button

    // 생년월일 날짜 정보 가져오기
    private lateinit var dpSpinner: DatePicker // datepicker dialog (데이트정보)


    // Firebase Realtime Database 연동을 위한 객체
    private lateinit var mDbRef: DatabaseReference
    // Firebase Authentication 연동을 위한 객체
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Firebase RealtimeDatabase 초기화
        mDbRef = Firebase.database.reference
        // Firebase Authentication 초기화
        mAuth = Firebase.auth

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextid = findViewById(R.id.editTextid)
        editTextPassword = findViewById(R.id.editTextPassword)


        // 날짜 및 캘린더 받아오기
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        // 다이어로그

        //val mydate = DatePickerDialog.OnDateSetListener()

        // 날짜 표시할 텍스트 뷰 가져오기
        val textview = findViewById<TextView>(R.id.birthText)

        btnSignUp = findViewById(R.id.btnSignUp)  // 날짜 선택 버튼 가져오기
        dpSpinner = findViewById(R.id.dpSpinner)


        btnSignUp.setOnClickListener {
            // 회원가입 버튼을 클릭했을 때의 동작을 구현하세요.
            val nickname = editTextUsername.text.toString()
            val useremail = editTextid.text.toString()
            val password = editTextPassword.text.toString()
            //val birth = dpSpinner.text.toString()

            // 여기에 실제 회원가입 로직을 추가하세요.
            // 예를 들어, 서버에 데이터를 전송하거나 로컬 DB에 저장하는 코드를 작성할 수 있습니다.
            // 실제 로직은 서버 통신이나 데이터베이스 저장 등으로 구현해야 합니다.

            // 회원가입 정보 Firebase RealtimeDatabase 연동
            Register(useremail, password, nickname)
        }
    }


    //firebase Register 연동
    private fun Register(useremail:String, password: String, nickname: String) {

        mAuth.createUserWithEmailAndPassword(useremail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "성공적으로 가입이 완료되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent: Intent = Intent(
                        this@SignUpActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)

                    // User Class에 데이터를 담아서
                    // Realtime Database로 전송
                    addUserToDB(useremail, password, nickname, mAuth.currentUser?.uid!!)
                } else {
                    Toast.makeText(
                        this,
                        "회원가입에 실패하였습니다. 다시 시도해주세요. 비밀번호는 6자 이상이어야 합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Sign Up", "Error: ${task.exception}")
                }
            }
    }

    private fun addUserToDB(useremail: String, password: String, uId: String, nickname: String){
        mDbRef.child("user").child(useremail).setValue(User(useremail, nickname,password,uId))
        //  이메일ㅎ 하위로 child를 만들어서 각 정보들을 저장한다.
    }


    }

