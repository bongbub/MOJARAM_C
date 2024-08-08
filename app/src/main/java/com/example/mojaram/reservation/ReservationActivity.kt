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


    // intent로부터 salon 데이터를 받아와, ViewModel에 전달, 화면에 보여줌
    private fun loadSalonData() {
        intent.getParcelableExtra(  // 다른 activity나 fragment로 부터 'SalonModel'객체 받아오기
            MapFragment.SALON_DETAIL_KEY,
            SalonModel::class.java
        )?.let { salon ->
            viewModel.changeSalonDetail(salon)
            // 받아온 데이터를 'ViewModel'에 전달해, 상태를 업데이트
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


    // 날짜 선택 시 발생하는 이벤트 + 예약 가능한 시간 불러오기
    private fun setDateSelectListener() {
        binding.calendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val formatter = DecimalFormat("00")
            val formattedMonth = formatter.format(month+1)
            viewModel.changeSelectedDate("${year}-${formattedMonth}-${dayOfMonth}")
            // 선택한 날짜 'ViewModel'에 전달
        }

        // 선택된 날짜가 변경되면 해당 날짜의 예약 정보 가져오기
        collectWhenStarted(viewModel.selectedDate) {
            viewModel.getReservations()
        }
    }

    private fun setTimeTables() {
        binding.gridlayoutTimeTables.columnCount = 5 // 그리드 레이아웃 수
        // 예약 가능한 시간 정보 수집 후 화면에 표시
        collectWhenStarted(viewModel.reservationTimeSections) { reservationTimeList ->
            binding.gridlayoutTimeTables.removeAllViewsInLayout() // 이전 표시된 시간표 지우기
            
            reservationTimeList.forEach { reservationTime ->
                // 시간표 레이아웃 생성
                var item = ItemTimeTablesBinding.inflate(layoutInflater, binding.gridlayoutTimeTables, false)
                val params = LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f))
                params.setGravity(Gravity.FILL)
                item.root.layoutParams = params
                item.reserveTimeEntity = reservationTime
                item.setTimeItemBackground(reservationTime.status)

                // 사용자가 특정 시간을 선택했을 때, 예약 가능 여부에 따라 상태 변경과 UI 업데이트
                item.root.setOnClickListener {
                    val status = (item.reserveTimeEntity as ReserveTimeEntity).status
                    if(status != TimeTableStatusEnum.Disable && status != TimeTableStatusEnum.None ) {
                        if(status == TimeTableStatusEnum.Available) {
                            viewModel.changeTimeSelected(reservationTime, TimeTableStatusEnum.Selected)
                        } else {
                            viewModel.changeTimeSelected(reservationTime, TimeTableStatusEnum.Available)
                        }
                        // 레이아웃 다시 그리기
                        item.root.requestLayout()
                    }
                }
                binding.gridlayoutTimeTables.addView(item.root)
            }
            binding.gridlayoutTimeTables.requestLayout()
        }
    }

    private fun completeReservation() {
        binding.textviewReservation.setOnClickListener {
            // 예약 완료 버튼이 눌리면, 'ViewModel'에 예약 요청 전송
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
    
    // 시간의 배경을 예약 상태에 따라 변경
    private fun ItemTimeTablesBinding.setTimeItemBackground(status: TimeTableStatusEnum) {
        reserveTimeEntity = reserveTimeEntity?.copy(status = status)
        root.setBackgroundResource(status.background)
    }
}