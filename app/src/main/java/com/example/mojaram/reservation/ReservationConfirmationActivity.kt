package com.example.mojaram.reservation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.databinding.ActivityReservationConfirmationBinding

class ReservationConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationConfirmationBinding
    
    // 예약된 시간과 날짜를 표시하는 클래스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = intent.getStringExtra("DATE")
        val time = intent.getStringExtra("TIME")
        binding.tvConfirmationMessage.text = "예약이 확인되었습니다. 날짜: $date, 시간: $time"
    }
}