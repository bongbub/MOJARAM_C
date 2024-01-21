package com.example.mojaram.ui.login

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
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



class SignUpActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextid: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var btnSignUp: Button

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
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            // 회원가입 버튼을 클릭했을 때의 동작을 구현하세요.
            val username = editTextUsername.text.toString()
            val userid = editTextid.text.toString()
            val password = editTextPassword.text.toString()

            // 여기에 실제 회원가입 로직을 추가하세요.
            // 예를 들어, 서버에 데이터를 전송하거나 로컬 DB에 저장하는 코드를 작성할 수 있습니다.
            // 실제 로직은 서버 통신이나 데이터베이스 저장 등으로 구현해야 합니다.

            // 회원가입 정보 Firebase RealtimeDatabase 연동
            Register(userid, password)
        }
    }


    private fun Register(userid:String, password: String) {

        mAuth.createUserWithEmailAndPassword(userid, password)
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
                    addUserToDB(userid, password, mAuth.currentUser?.uid!!)
                } else {
                    Toast.makeText(
                        this,
                        "회원가입에 실패하였습니다. 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Sign Up", "Error: ${task.exception}")
                }
            }
    }

    private fun addUserToDB(userid: String, password: String, uId: String){
        mDbRef.child("user").child(uId).setValue(User(userid, password, uId))
    }

    /*
    private fun Register(userid: String, password: String, username:String){
        mAuth.createUserWithEmailAndPassword(userid, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {
                    // 예외처리
                    if(TextUtils.isEmpty(username) ||
                        TextUtils.isEmpty(userid) ||
                        TextUtils.isEmpty(password)){
                        Toast.makeText(this@SignUpActivity,
                            "모든 정보를 기재해주세요.", Toast.LENGTH_SHORT).show()
                    }
                    if(password.length < 6){
                        Toast.makeText(this@SignUpActivity,
                            "비밀번호는 6자리 이상 입력해주세요.",
                            Toast.LENGTH_SHORT).show()
                    }

                    // HashMap 생성
                    val firebaseUser : FirebaseUser? = mAuth.currentUser
                    val usersid = firebaseUser?.uid
                    val ref = FirebaseDatabase.getInstance().reference.child("Users").child(usersid!!)
                    val hashMap: HashMap<String, Any> = HashMap()

                    hashMap["userid"] = usersid
                    hashMap["email"] = userid
                    hashMap["password"] = editTextPassword

                    // firebase database에 유저 추가 (setValue)
                    ref.setValue(hashMap).addOnCompleteListener(object :OnCompleteListener<Void>{
                        override fun onComplete(p0: Task<Void>) {
                            if(p0.isSuccessful){
                                Toast.makeText(this@SignUpActivity,
                                    "회원가입이 성공적으로 완료되었습니다. 로그인해주세요!",
                                    Toast.LENGTH_SHORT).show()
                                val intent: Intent = Intent(this@SignUpActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                            else{
                                Toast.makeText(this@SignUpActivity,
                                    "회원가입에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                                Log.d("Register Error", "Error: {${p0.exception}")
                            }}})}})}
     */
/*
            {task->
                if(task.isSuccessful){
                    // 만약 Firebase에 저장되어있지 않은 계정일 경우
                    // 입력 받은 계정을 새롭게 등록한다
                    goToMainActivity(task.result?.user)

                }else if(task.exception?.message.isNullOrEmpty()){
                    Toast.makeText(this,task.exception?.message,
                        Toast.LENGTH_SHORT).show()
                }else{
                    // 입력 정보가 이미 DB에 있는 경우
                    Toast.makeText(this@SignUpActivity, "존재하는 이메일입니다. 로그인 해주세요",
                        Toast.LENGTH_SHORT).show()
                }
        }
 */
    }

    /*
    // 회원가입 성공 시 로그인 화면으로 전환
    fun goToMainActivity(user: FirebaseUser?){
        // Firebase에 등록된 계정일 경우만 메인화면으로 이동!
        if(user != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
     */

