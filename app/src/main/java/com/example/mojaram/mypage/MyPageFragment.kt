package com.example.mojaram.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mojaram.ReviewWriteActivity
import com.example.mojaram.admin.AdminMain
import com.example.mojaram.coupon.CouponListActivity
import com.example.mojaram.databinding.FragmentMyPageBinding
import com.example.mojaram.setting.SettingActivity
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

        // 유저 정보 로드
        loadUserInfo()
        initview()
    }
    fun initview(){
        binding.buttonMyReviews.mypageMenuLayout.setOnClickListener({v ->
            Log.d("SSSS","리뷰작성으로가기")

            startActivity(Intent(requireContext(),ReviewWriteActivity::class.java))
        })

        // 예약 상태 확인
        binding.buttonMyReservation.mypageMenuLayout.setOnClickListener { v ->
            Log.d("SSSS", "예약 상태 확인 가기")
            startActivity(Intent(requireContext(), AdminMain::class.java))
        }

        // 쿠폰함
        binding.buttonChat.mypageMenuLayout.setOnClickListener { v ->
            Log.d("SSSS", "쿠폰함 가기")
            startActivity(Intent(requireContext(), CouponListActivity::class.java))
        }

        // 설정
        binding.buttonSetting.mypageMenuLayout.setOnClickListener { v ->
            Log.d("SSSS", "설정 가기")
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }
    }

    private fun navToProfileSetting() {
        binding.cardviewMyInfo.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileSettingActivity::class.java))
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
