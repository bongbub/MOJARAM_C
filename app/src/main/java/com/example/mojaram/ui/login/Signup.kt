package com.example.mojaram.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.R
import com.example.mojaram.LogInActivity
import com.example.mojaram.User
import com.example.mojaram.admin.AdminMain
// Firebase 연동을 위한 import
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import java.util.Calendar


class SignUpActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextid: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnSignUp: Button
    // 생년월일
    private lateinit var Userbirthday : EditText
    
    // 비밀번호 체크
    private lateinit var editTextpassword_check: EditText
    private lateinit var textview_passcheck:TextView //일치문자

    // 성별
    private lateinit var checkMale : CheckBox
    private lateinit var checkFemale : CheckBox

    // 생년월일 날짜 정보 가져오기
    private lateinit var dpSpinner: DatePicker // datepicker dialog (데이트정보)

    private var db = Firebase.firestore
    // Firebase Authentication 연동을 위한 객체
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = Firebase.auth

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextid = findViewById(R.id.editTextid)
        editTextPassword = findViewById(R.id.editTextPassword)
        textview_passcheck = findViewById(R.id.textview_passcheck)
        editTextpassword_check = findViewById(R.id.editTextPassword_check)
        btnSignUp = findViewById(R.id.btnSignUp)

        // 생년월일 가져오기
        Userbirthday = findViewById(R.id.birthText)

        // 성별 판별
        checkMale = findViewById(R.id.malecheck)
        checkFemale = findViewById(R.id.femalecheck)


        // 비밀번호 일치 여부
        editTextpassword_check.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(editTextPassword.text.toString() == s.toString()){
                    textview_passcheck.text = "일치합니다."
                }else{
                    textview_passcheck.text = "일치하지 않습니다."
                }
            }
            
        })

        btnSignUp.setOnClickListener {
            // 회원가입 버튼을 클릭했을 때의 동작을 구현하세요.
            val nickname = editTextUsername.text.toString().trim()
            val useremail = editTextid.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val passwordCheck = textview_passcheck.text.toString().trim()
            val userbirth = Userbirthday.text.toString().trim()


            // 성별 판별
            val userGender = if(checkMale.isChecked) "male" else "female"


            if (nickname.isEmpty() || useremail.isEmpty() || password.isEmpty() || userbirth.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 회원가입 정보 Firebase RealtimeDatabase 연동
            Register(nickname, useremail, password, userGender, userbirth) //여기 순서 중요
        }
    }


    //firebase Register 연동
    private fun Register(nickname: String, useremail:String, password: String, userGender: String, userbirth: String) {

        mAuth.createUserWithEmailAndPassword(useremail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "성공적으로 가입이 완료되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@SignUpActivity, LogInActivity::class.java)
                    startActivity(intent)
                    finish()

                    // User Class에 데이터를 담아서
                    // Firestore로 전송
                    addUserToFirestore(nickname, useremail, password, userGender, userbirth, mAuth.currentUser?.uid!!)
                } else {
                    val errorMessage = task.exception?.message ?: "회원가입에 실패하였습니다. 다시 시도해주세요. 비밀번호는 6자 이상이어야 합니다."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.d("Sign Up", "Error: ${task.exception}")
                }
            }
    }
    private fun addUserToFirestore(nickname: String, useremail: String, password: String, userGender: String, userbirth: String, uId: String){
        val user = User(useremail, nickname,  password, userGender,userbirth, uId)
        db.collection("user_customer").document(useremail).set(user)
            .addOnSuccessListener {
                Log.d("Firestore","파이어스토어 회원 정보 업로드 완료")
            }
            .addOnFailureListener{ e ->
                Log.w("Firestore", "파이어스토어 회원 정보 업로드 오류", e)
            }
    }
}

