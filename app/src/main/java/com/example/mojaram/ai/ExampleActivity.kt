package com.example.mojaram.ai

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityExampleBinding

class ExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageviewBack.setOnClickListener {
            finish()
        }

        binding.textviewOk.setOnClickListener {
            startActivity(Intent(this, CreateStyleActivity::class.java))
        }
    }
}