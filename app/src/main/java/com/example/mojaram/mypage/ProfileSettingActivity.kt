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
import com.google.firebase.auth.FirebaseAuth

class ProfileSettingActivity: AppCompatActivity() {
    // Firebase auth 사용하기 위해
    var auth: FirebaseAuth? = null

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

        binding.imageviewBack.setOnClickListener {
            finish()
        }
        setInterestCategories()

        auth = FirebaseAuth.getInstance()

        val logoutbtn : Button = findViewById(R.id.logoutbtn)


        logoutbtn.setOnClickListener {
            // 로그아웃 버튼을 눌렀을 때 로그아웃
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LogInActivity::class.java)
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
}