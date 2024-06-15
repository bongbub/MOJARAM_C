package com.example.mojaram.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.LogInActivity
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityProfileSettingBinding
import com.example.mojaram.utils.HorizontalRecylerViewDivider
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class ProfileSettingActivity: AppCompatActivity() {
    // Firebase auth 사용하기 위해
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()


    private lateinit var binding: ActivityProfileSettingBinding
    private val categories = listOf(
        "부분탈모", "남성", "인조모", "인모"
    ).map {
        InterestCategoryEntity(category = it, selected = false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        loadUserInfo()

        binding.imageviewBack.setOnClickListener {
            finish()
        }
        setInterestCategories()

        val logoutbtn : Button = findViewById(R.id.logoutbtn)


        logoutbtn.setOnClickListener {
            // 로그아웃 버튼을 눌렀을 때 로그아웃
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //로그인 액비 시작 전에 현재 액비 클리어 하고 새로운 테스크
            Log.d("ProfilesettingActivity","Logout")
            startActivity(intent)
            finish()
        }
    }

    private fun setInterestCategories() {
        binding.recyclerviewCategories.run {
            addItemDecoration(HorizontalRecylerViewDivider(15))
            layoutManager = FlexboxLayoutManager(this@ProfileSettingActivity).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
            }
            adapter = CategoryListAdapter()
            (adapter as CategoryListAdapter).submitList(categories)
        }
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            // Firestore에서 사용자 기본 정보를 가져와, userType과 userGender를 확인
            val userBasicInfoRef = db.collection("user_customer").document(user.email!!)
            userBasicInfoRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nickname = document.getString("nickname")
                    val email = document.getString("email")
                    val userGender = document.getString("userGender")
                    updateUI(nickname, email) // UI 업데이트
                } else {
                    Log.d("Firestore", "해당 정보 찾을 수 없음")
                }
            }.addOnFailureListener { e ->
                Log.w("Firestore", "파이어스토어 기본 정보 가져오기 오류", e)
            }
        } else {
            Log.d("Firestore", "해당 유저 없음")
        }
    }
    private fun updateUI(nickname: String?, email: String?) {
        binding.textviewName.text = nickname ?: "No nickname"
        binding.textviewId.text = email ?: "No Email"}

//    private fun loadDetailedUserInfo(nickname: String?, email: String?, userGender: String?) {
//        // 파베에서 상세 유저 정보 가져오기
//        val userDocRef = db.collection("user_customer").document(email!!)
//        userDocRef.get().addOnSuccessListener { document ->
//            if (document != null && document.exists()) {
//                val detailedNickname = document.getString("nickname")
//                val detailedEmail = document.getString("email")
//                updateUI(detailedNickname, detailedEmail)
//            } else {
//                Log.d("Firestore", "해당 문서 찾을 수 없음")
//            }
//        }.addOnFailureListener { e ->
//            Log.w("Firestore", "파이어스토어 상세 정보 가져오기 오류", e)
//        }
//    }

}
