package com.example.mojaram.ui.login

import BirthTextWatcher
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
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

    // 관리자 등급 분할
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioAdmin: RadioButton
    private lateinit var radioUser: RadioButton

    // 성별
    private lateinit var checkMale : CheckBox
    private lateinit var checkFemale : CheckBox

    // 생년월일 날짜 정보 가져오기
    private lateinit var dpSpinner: DatePicker // datepicker dialog (데이트정보)


    // 전화번호
    private lateinit var phone_number : EditText
    private val watcher = PhoneNumberFormattingTextWatcher()


    // Firebase FireStore 연동을 위한 객체
    private var db = Firebase.firestore
    // Firebase Authentication 연동을 위한 객체
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Firebase Authentication 초기화
        mAuth = Firebase.auth

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextid = findViewById(R.id.editTextid)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnSignUp = findViewById(R.id.btnSignUp)

        // 생년월일 가져오기
        Userbirthday = findViewById(R.id.birthText)
        Userbirthday.addTextChangedListener(BirthTextWatcher(Userbirthday))


        // 등급 판별을 위한 변수
        radioGroup = findViewById(R.id.radioGroup)
        radioAdmin = findViewById(R.id.radioAdmin)
        radioUser = findViewById(R.id.radioUser)

        // 성별 판별
        checkMale = findViewById(R.id.malecheck)
        checkFemale = findViewById(R.id.femalecheck)

        // 전화번호
        phone_number = findViewById(R.id.phone_number)
        phone_number.addTextChangedListener(watcher)  //전화번호 자동 하이픈 포맷

/*
        // 날짜 및 캘린더 받아오기
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

 */

        //dpSpinner = findViewById(R.id.dpSpinner)



        btnSignUp.setOnClickListener {
            // 회원가입 버튼을 클릭했을 때의 동작을 구현하세요.
            val UserName = editTextUsername.text.toString().trim()
            val useremail = editTextid.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val userbirth = Userbirthday.text.toString().trim()
            val phone_number = phone_number.text.toString().trim()
            // 등급 판별
            val userType = if(radioAdmin.isChecked) "admin" else "customer"  // 어드민 라디오를 체크하면 관리자 판별
            // 성별 판별
            val userGender = if(checkMale.isChecked) "male" else "female"


            if (UserName.isEmpty() || useremail.isEmpty() || password.isEmpty() || userbirth.isEmpty() || phone_number.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 회원가입 정보 Firebase RealtimeDatabase 연동
            Register(useremail,UserName,  password, userType, userGender, userbirth, phone_number) //여기 순서 중요
        }
    }


    //firebase Register 연동
    private fun Register(useremail:String, UserName: String, password: String, userType: String, userGender: String, userbirth: String,phone_number: String) {

        mAuth.createUserWithEmailAndPassword(useremail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "성공적으로 가입이 완료되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 관리자로 가입하였다면 관리자 페이지 출력
                    val intent = if(userType =="관리자"){
                        Intent(this@SignUpActivity, AdminMain::class.java)
                    }else{
                        Intent(this@SignUpActivity, LogInActivity::class.java)
                    }

                    startActivity(intent)
                    finish()

                    // User Class에 데이터를 담아서
                    // Firestore로 전송
                    addUserToFirestore(useremail, UserName, password, userType, userGender, userbirth, phone_number, mAuth.currentUser?.uid!!)
                } else {
                    val errorMessage = task.exception?.message ?: "회원가입에 실패하였습니다. 다시 시도해주세요. 비밀번호는 6자 이상이어야 합니다."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToFirestore( useremail: String, UserName: String, password: String, userType: String, userGender: String, userbirth: String, phone_number:String, uId: String){
        val user = User(useremail, UserName, password, userType, userGender,userbirth,phone_number, uId)
        val collection = if(userType == "admin") "user_admin" else "user_customer"
        db.collection(collection).document(useremail).set(user)
            .addOnSuccessListener {
                Log.d("Firestore","파이어스토어 회원 정보 업로드 완료")
            }
            .addOnFailureListener{ e ->
                Log.w("Firestore", "파이어스토어 회원 정보 업로드 오류", e)
            }
    }
}

