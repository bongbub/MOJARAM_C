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
    }
    fun initview(){
        binding.textWriteReview.setOnClickListener({ v ->
            Log.d("SSSS","textWriteReview누름")
            startActivity(Intent(this,MyReviews::class.java))
        })
    }
}