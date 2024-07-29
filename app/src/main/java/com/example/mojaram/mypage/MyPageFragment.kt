package com.example.mojaram.mypage

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mojaram.admin.AdminSignUp
import com.example.mojaram.databinding.FragmentMyPageBinding
import com.example.mojaram.utils.AutoClearedValue
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class MyPageFragment: Fragment() {
    private var binding by AutoClearedValue<FragmentMyPageBinding>()

    // 회원 정보를 firestore에서 불러오기 위한 객체
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMyPageBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navToProfileSetting()
        navToAdminSignUp()  // 관리자 회원으로 전환하기 누르면 넘어가는 함수 2024/7/29
        
        // 유저 정보 로드
        loadUserInfo()
    }

    private fun navToProfileSetting() {
        binding.cardviewMyInfo.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileSettingActivity::class.java))
        }
    }

    // 관리자 회원으로 전환하는 함수
    private fun navToAdminSignUp(){
        binding.imageviewSugAdmin.setOnClickListener{
            startActivity(Intent(requireContext(), AdminSignUp::class.java))
        }
    }

    // 유저 정보 로드하는 함수 선언
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
                    loadDetailedUserInfo(nickname, email, userGender)
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
        binding.textviewEmail.text = email ?: "No Email"}

    private fun loadDetailedUserInfo(nickname: String?, email: String?, userGender: String?) {
        // 파베에서 상세 유저 정보 가져오기
        val userDocRef = db.collection("user_customer").document(email!!)
        userDocRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val detailedNickname = document.getString("nickname")
                val detailedEmail = document.getString("email")
                updateUI(detailedNickname, detailedEmail)
            } else {
                Log.d("Firestore", "해당 문서 찾을 수 없음")
            }
        }.addOnFailureListener { e ->
            Log.w("Firestore", "파이어스토어 상세 정보 가져오기 오류", e)
        }
    }

}
