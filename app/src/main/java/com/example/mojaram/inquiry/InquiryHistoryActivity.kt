package com.example.mojaram.inquiry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityInquiryHistoryBinding

class InquiryHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInquiryHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inquiry_history)

        with(binding) {
            with(imageviewBack) {
                setOnClickListener {
                    finish()
                }
            }
        }
    }
}