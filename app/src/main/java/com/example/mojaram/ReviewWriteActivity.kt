package com.example.mojaram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.databinding.ActivityMyreviewssBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewWriteActivity :AppCompatActivity() {
    private lateinit var binding: ActivityMyreviewssBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMyreviewssBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("SSSS","aaaonCreate")
        initview()

        binding.imgGroup.setOnClickListener {
            Log.d("SSSS", "뒤로가기 버튼 클릭")
            finish()  // 현재 액티비티 종료
        }

    }
    fun initview(){
        binding.textWriteReview.setOnClickListener({ v ->
            Log.d("SSSS","textWriteReview누름")
            startActivity(Intent(this,MyReviews::class.java))
        })
    }
}