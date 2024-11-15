package com.example.mojaram.serviceterm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityServiceTermBinding

class ServiceTermActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceTermBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_term)

        with(binding) {
            with(imageviewBack) {
                setOnClickListener {
                    finish()
                }
            }
        }
    }
}