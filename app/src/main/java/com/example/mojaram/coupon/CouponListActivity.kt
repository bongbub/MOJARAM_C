package com.example.mojaram.coupon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityCouponListBinding

class CouponListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCouponListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon_list)

        with(binding) {
            with(imageviewBack) {
                setOnClickListener {
                    finish()
                }
            }
        }
    }
}