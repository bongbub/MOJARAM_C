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
import com.example.mojaram.admin.AdminSignUp
import com.example.mojaram.databinding.ActivityAdminSignUpBinding
import com.example.mojaram.databinding.ActivitySignupBinding
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
    private lateinit var btnShopRegi: Button
    private lateinit var editTextnumber: EditText

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

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = Firebase.auth

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextid = findViewById(R.id.editTextid)
        editTextPassword = findViewById(R.id.editTextPassword)
        textview_passcheck = findViewById(R.id.textview_passcheck)
        editTextpassword_check = findViewById(R.id.editTextPassword_check)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnShopRegi = findViewById(R.id.btnShopRegi)
        editTextnumber = findViewById(R.id.numberText)

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
            val editTextnumber = editTextnumber.text.toString().trim()


            // 성별 판별
            val userGender = if(checkMale.isChecked) "male" else "female"


            if (nickname.isEmpty() || useremail.isEmpty() || password.isEmpty() || userbirth.isEmpty() || password.isEmpty() || editTextnumber.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if (passwordCheck.isEmpty()){
                Toast.makeText(this, "비밀번호 재입력을 확인해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 회원가입 정보 Firebase RealtimeDatabase 연동
            Register(nickname, useremail, password, editTextnumber,userGender, userbirth) //여기 순서 중요
        }

        // 매장 등록 버튼을 눌렀을 때
        btnShopRegi.setOnClickListener{

            val nickname = editTextUsername.text.toString().trim()
            val useremail = editTextid.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val passwordCheck = textview_passcheck.text.toString().trim()
            val userbirth = Userbirthday.text.toString().trim()
            val userGender = if(checkMale.isChecked) "male" else "female"


            if (nickname.isEmpty() || useremail.isEmpty() || password.isEmpty() || userbirth.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if (passwordCheck.isEmpty()){
                Toast.makeText(this, "비밀번호 재입력을 확인해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // 기존 입력한 정보를 갖고 매장 등록 페이지로 전환
            val intent = Intent(this, AdminSignUp::class.java).apply {
                putExtra("nickname", editTextUsername.text.toString().trim())
                putExtra("useremail",editTextid.text.toString().trim())
                putExtra("userGender",if(checkMale.isChecked)"male" else "female")
                putExtra("userbirth", Userbirthday.text.toString().trim())
                putExtra("password", editTextPassword.text.toString().trim())
                putExtra("number", editTextnumber.text.toString().trim())
            }
            startActivity(intent)
        }

        // 전화번호 형식
        binding.numberText.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true

                val currentText = s.toString().replace("-", "")
                val formattedText = when {
                    currentText.length > 7 -> "${currentText.substring(0, 3)}-${currentText.substring(3, 7)}-${currentText.substring(7)}"
                    currentText.length > 3 -> "${currentText.substring(0, 3)}-${currentText.substring(3)}"
                    else -> currentText
                }

                binding.numberText.setText(formattedText)
                binding.numberText.setSelection(formattedText.length)

                isFormatting = false
            }
        })
    }

    //firebase Register 연동
    private fun Register(nickname: String, useremail:String, password: String, editTextnumber:String, userGender: String, userbirth: String) {

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
                    addUserToFirestore(nickname, useremail, password, editTextnumber, userGender, userbirth, mAuth.currentUser?.uid!!)
                } else {
                    val errorMessage = task.exception?.message ?: "회원가입에 실패하였습니다. 다시 시도해주세요. 비밀번호는 6자 이상이어야 합니다."
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.d("Sign Up", "Error: ${task.exception}")
                }
            }
    }
    private fun addUserToFirestore(nickname: String, useremail: String, password: String, editTextnumber: String, userGender: String, userbirth: String, uId: String){
        val user = User(useremail, nickname,  password,editTextnumber, userGender,userbirth, uId)
        db.collection("user_customer").document(useremail).set(user)
            .addOnSuccessListener {
                Log.d("Firestore","파이어스토어 회원 정보 업로드 완료")
            }
            .addOnFailureListener{ e ->
                Log.w("Firestore", "파이어스토어 회원 정보 업로드 오류", e)
            }
    }
}

