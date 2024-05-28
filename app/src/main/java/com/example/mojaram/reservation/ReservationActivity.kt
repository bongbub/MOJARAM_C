package com.example.mojaram.reservation

import android.os.Bundle
import android.view.Gravity
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityReservationBinding
import com.example.mojaram.databinding.ItemTimeTablesBinding

class ReservationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private val timeTables = (10..24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackButton()
        setDateSelectListener()
        setTimeTables()
    }

    private fun setBackButton() {
        binding.imageviewBack.setOnClickListener {
            finish()
        }
    }

    private fun setDateSelectListener() {
        binding.calendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->

        }
    }

    private fun setTimeTables() {
        binding.gridlayoutTimeTables.run {
            columnCount = 5
            timeTables.forEachIndexed { idx, time ->
                val reserveTimeEntity = ReserveTimeEntity(
                    time = if(time <= 21) "${time}:00" else "",
                    status = if(time > 21) TimeTableStatusEnum.None else TimeTableStatusEnum.Available
                )

                var item = ItemTimeTablesBinding.inflate(layoutInflater, binding.gridlayoutTimeTables, false)
                val params = LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f))
                params.setGravity(Gravity.FILL)
                item.root.layoutParams = params
                item.reserveTimeEntity = reserveTimeEntity
                item.setTimeItemBackground(reserveTimeEntity.status)
                item.root.setOnClickListener {
                    val status = (item.reserveTimeEntity as ReserveTimeEntity).status
                    if(status != TimeTableStatusEnum.Disable && status != TimeTableStatusEnum.None ) {
                        if(status == TimeTableStatusEnum.Available) {
                            item.setTimeItemBackground(TimeTableStatusEnum.Selected)
                        } else {
                            item.setTimeItemBackground(TimeTableStatusEnum.Available)
                        }
                        item.root.requestLayout()
                    }
                }
                addView(item.root)
            }
            binding.gridlayoutTimeTables.requestLayout()
        }
    }

    private fun ItemTimeTablesBinding.setTimeItemBackground(status: TimeTableStatusEnum) {
        reserveTimeEntity = reserveTimeEntity?.copy(status = status)
        root.setBackgroundResource(status.background)
    }
}