package com.example.mojaram.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivitySettingBinding
import com.example.mojaram.faq.FaqActivity
import com.example.mojaram.inquiry.InquiryActivity
import com.example.mojaram.inquiry.InquiryHistoryActivity
import com.example.mojaram.serviceterm.ServiceTermActivity
import com.example.mojaram.withdrawal.WithdrawalActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        with(binding) {
            with(imageviewBack) {
                setOnClickListener {
                    finish()
                }
            }

            with(btnInquiry) {
                setOnClickListener {
                    startActivity(Intent(context, InquiryActivity::class.java))
                }
            }

            with(btnInquiryHistory) {
                setOnClickListener {
                    startActivity(Intent(context, InquiryHistoryActivity::class.java))
                }
            }

            with(btnFaq) {
                setOnClickListener {
                    startActivity(Intent(context, FaqActivity::class.java))
                }
            }

            with(btnServiceTerm) {
                setOnClickListener {
                    startActivity(Intent(context, ServiceTermActivity::class.java))
                }
            }

            with(btnWithdrawal) {
                setOnClickListener {
                    startActivity(Intent(context, WithdrawalActivity::class.java))
                }
            }
        }
    }
}