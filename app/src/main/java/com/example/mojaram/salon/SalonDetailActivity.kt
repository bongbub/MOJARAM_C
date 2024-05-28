package com.example.mojaram.salon

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivitySalonDetailBinding
import com.example.mojaram.reservation.ReservationActivity
import kotlin.math.abs

class SalonDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySalonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listenScrollState()
        clickReserve()
    }

    private fun listenScrollState() {
        binding.appbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                setBackButton(true)
                setSalonNameVisibility(true)
            } else if (verticalOffset == 0) {
                setBackButton(false)
                setSalonNameVisibility(false)
            }
        }
    }

    private fun setBackButton(collapse: Boolean) {
        binding.imageviewBack.run {
            setImageResource(
                if(collapse) R.drawable.ic_back_black else R.drawable.ic_back_white
            )
            setOnClickListener {
                finish()
            }
        }
    }

    private fun setSalonNameVisibility(collapse: Boolean) {
        binding.textviewSalonNameCollapse.visibility = if(collapse) View.VISIBLE else View.GONE
    }

    private fun clickReserve() {
        binding.textviewReservation.setOnClickListener {
            startActivity(Intent(this, ReservationActivity::class.java))
        }
    }
}