package com.example.mojaram.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.databinding.ActivityProfileSettingBinding
import com.example.mojaram.utils.HorizontalRecylerViewDivider
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class ProfileSettingActivity: AppCompatActivity() {
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