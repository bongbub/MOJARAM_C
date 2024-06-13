package com.example.mojaram.reservation

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityReservationBinding
import com.example.mojaram.databinding.ItemTimeTablesBinding
import com.example.mojaram.map.MapFragment
import com.example.mojaram.map.SalonModel
import com.example.mojaram.utils.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class ReservationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private val viewModel by viewModels<ReservationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSalonData()
        setBackButton()
        setDateSelectListener()
        setTimeTables()
        completeReservation()
    }

    private fun loadSalonData() {
        intent.getParcelableExtra(
            MapFragment.SALON_DETAIL_KEY,
            SalonModel::class.java
        )?.let { salon ->
            viewModel.changeSalonDetail(salon)
        }
        collectWhenStarted(viewModel.salonDetail) { salon ->
            binding.textviewSalonName.text = salon?.shopName
        }
    }

    private fun setBackButton() {
        binding.imageviewBack.setOnClickListener {
            finish()
        }
    }

    private fun setDateSelectListener() {
        binding.calendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val formatter = DecimalFormat("00")
            val formattedMonth = formatter.format(month+1)
            viewModel.changeSelectedDate("${year}-${formattedMonth}-${dayOfMonth}")
        }

        collectWhenStarted(viewModel.selectedDate) {
            viewModel.getReservations()
        }
    }

    private fun setTimeTables() {
        binding.gridlayoutTimeTables.columnCount = 5
        collectWhenStarted(viewModel.reservationTimeSections) { reservationTimeList ->
            binding.gridlayoutTimeTables.removeAllViewsInLayout()
            reservationTimeList.forEach { reservationTime ->
                var item = ItemTimeTablesBinding.inflate(layoutInflater, binding.gridlayoutTimeTables, false)
                val params = LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f))
                params.setGravity(Gravity.FILL)
                item.root.layoutParams = params
                item.reserveTimeEntity = reservationTime
                item.setTimeItemBackground(reservationTime.status)
                item.root.setOnClickListener {
                    val status = (item.reserveTimeEntity as ReserveTimeEntity).status
                    if(status != TimeTableStatusEnum.Disable && status != TimeTableStatusEnum.None ) {
                        if(status == TimeTableStatusEnum.Available) {
                            viewModel.changeTimeSelected(reservationTime, TimeTableStatusEnum.Selected)
                        } else {
                            viewModel.changeTimeSelected(reservationTime, TimeTableStatusEnum.Available)
                        }
                        item.root.requestLayout()
                    }
                }
                binding.gridlayoutTimeTables.addView(item.root)
            }
            binding.gridlayoutTimeTables.requestLayout()
        }
    }

    private fun completeReservation() {
        binding.btnConfirmReservation.setOnClickListener {
            viewModel.reservation { result ->
                if(result) {
                    Toast.makeText(this, "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "예약에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun ItemTimeTablesBinding.setTimeItemBackground(status: TimeTableStatusEnum) {
        reserveTimeEntity = reserveTimeEntity?.copy(status = status)
        root.setBackgroundResource(status.background)
    }
}