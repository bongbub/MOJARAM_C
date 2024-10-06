package com.example.mojaram.inquiry

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityInquiryBinding

class InquiryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInquiryBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inquiry)

        with(binding) {
            with(imageviewBack) {
                setOnClickListener {
                    finish()
                }
            }

            with(etInquiry) {
                doAfterTextChanged {
                    tvIndex.text = "${it.toString().length}/500"
                }
            }
        }
    }
}