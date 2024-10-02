package com.example.mojaram.admin

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mojaram.LogInActivity
import com.example.mojaram.LogInActivity_MembersInjector
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityAdminSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminSignUp : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSignUpBinding

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SignUpActivity에서 전달된 정보 받기
        val nickname = intent.getStringExtra("nickname")
        val useremail = intent.getStringExtra("useremail")
        val userGender = intent.getStringExtra("userGender")
        val userbirth = intent.getStringExtra("userbirth")
        val password = intent.getStringExtra("password")

        binding.buttonSignUp.setOnClickListener {
            val shop_name = binding.editTextShopname.text.toString().trim()
            val shop_addr = binding.editTextShopaddress.text.toString()
            val shop_addr2 = binding.editTextShopaddress2.text.toString()
            val shop_num = binding.editTextShopNum.text.toString().trim()
            val shop_auth = binding.editTextAuth.text.toString()      //수정
            val shop_hour = binding.editTextShopHours.text.toString()
            val shop_price = binding.editTextShopPrice.text.toString()

            if (shop_name.isEmpty() || shop_addr.isEmpty() || shop_addr2.isEmpty() || shop_auth.isEmpty() || shop_hour.isEmpty() || shop_price.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase Auth에 사용자 등록
            registerUserWithFirebaseAuth(
                nickname,
                useremail!!,
                password!!,
                userGender,
                userbirth,
                shop_name,
                shop_addr,
                shop_addr2,
                shop_num,
                shop_auth,
                shop_hour,
                shop_price
            )
        }
    }

    private fun registerUserWithFirebaseAuth(
        nickname: String?, email: String, password: String, userGender: String?, userbirth: String?,
        shop_name: String?, shop_addr: String?, shop_addr2: String?, shop_num: String?,
        shop_auth: String?, shop_hour: String?, shop_price: String?
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 사용자 등록 성공
                    val uid = auth.currentUser?.uid // UID 가져오기
                    addAdminToFirestore(
                        nickname,
                        email,
                        password,
                        userGender,
                        userbirth,
                        shop_name,
                        shop_addr,
                        shop_addr2,
                        shop_num,
                        shop_auth,
                        shop_hour,
                        shop_price,
                        uid // 올바른 UID 전달
                    )
                } else {
                    // 사용자 등록 실패
                    Toast.makeText(this, "가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                    Log.w("FirebaseAuth", "가입 오류", task.exception)
                }
            }
    }

    private fun addAdminToFirestore(
        nickname: String?,
        useremail: String?,
        password: String?,
        userGender: String?,
        userbirth: String?,
        shop_name: String?,
        shop_addr: String?,
        shop_addr2: String?,
        shop_num: String?,
        shop_auth: String?,
        shop_hour: String?,
        shop_price: String?,
        uid: String? // UID 추가
    ) {

        val adminData = mapOf(
            "nickname" to nickname,
            "useremail" to useremail,
            "password" to password,
            "userGender" to userGender,
            "userbirth" to userbirth,
            "shop_name" to shop_name,
            "shop_addr" to shop_addr,
            "shop_addr2" to shop_addr2,
            "shop_num" to shop_num,
            "shop_auth" to shop_auth,
            "shop_hour" to shop_hour,
            "shop_price" to shop_price,
            "role" to getNextRoleValue(), // 역할 값 증가 함수 호출
            "uid" to uid // UID를 여기에 올바르게 저장
        )

        db.collection("user_admin").document(useremail!!).set(adminData)
            .addOnSuccessListener {
                Toast.makeText(this, "요청이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                // 로그인 액티비티로 이동
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "요청 실패 : ${e.message}", Toast.LENGTH_SHORT).show()
                Log.w("Firestore", "파이어스토어에 관리자 정보 업로드 오류", e)
            }
    }

    // 역할 값 증가를 위한 함수
    private fun getNextRoleValue(): String {
        // Firestore에서 역할 값을 가져와서 다음 값을 계산
        // 예를 들어, Firestore에서 모든 admin 문서를 가져와서 역할 값을 파악하여 계산
        var nextRole = "001" // 기본값

        db.collection("user_admin")
            .get()
            .addOnSuccessListener { documents ->
                val roleNumbers = mutableListOf<Int>()
                for (document in documents) {
                    val role = document.getString("role")
                    if (role != null) {
                        roleNumbers.add(role.toInt())
                    }
                }
                if (roleNumbers.isNotEmpty()) {
                    nextRole = String.format("%03d", roleNumbers.maxOrNull()!! + 1) // 다음 역할 값 계산
                }
            }
        return nextRole
    }
}