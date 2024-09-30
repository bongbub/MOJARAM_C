package com.example.mojaram.reservation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mojaram.data.FirebaseDataSource
import com.example.mojaram.data.PreferenceManager
import com.example.mojaram.map.SalonModel
import com.example.mojaram.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val preferenceManager: PreferenceManager
): ViewModel() {
    // 예약 가능 시간 범위 설정
    val timeTables = (10..24)
    
    private val _reservationTimeSections = MutableStateFlow<List<ReserveTimeEntity>>(listOf())
    val reservationTimeSections = _reservationTimeSections.asStateFlow()

    private val _salonDetail = MutableStateFlow<SalonModel?>(null)
    val salonDetail = _salonDetail.asStateFlow()

    private val _selectedDate = MutableStateFlow<String>(dateFormat.format(Date()))
    val selectedDate = _selectedDate.asStateFlow()

    private val _reservations = MutableStateFlow<List<ReservationModel>>(emptyList())
    val reservations = _reservations.asStateFlow()

//    // 매장별 예약 분할 관리를 위한 코드 부여
//    private val _shopCode = MutableStateFlow<String?>(null)
//    val shopCode = _shopCode.asStateFlow()


    init {
        initReservationTimeSections()
        getReservations()
    }

    
    // 예약 시간 테이블 초기화
    private fun initReservationTimeSections() {
        timeTables.mapIndexed { idx, time ->
            ReserveTimeEntity(
                time = if(time <= 21) "${time}:00" else "",
                status = if(time > 21) TimeTableStatusEnum.None else TimeTableStatusEnum.Available
            )
        }.let {
            _reservationTimeSections.value = it
        }
    }

    // Salon 정보 업데이트
    fun changeSalonDetail(salonModel: SalonModel) {
        _salonDetail.value = salonModel
    }

    
    // 선택 날짜 정보 업데이트
    fun changeSelectedDate(date: String) {
        _selectedDate.value = date
    }

    
    // 예약 시간의 상태 변경
    fun changeTimeSelected(reservationTime: ReserveTimeEntity, status: TimeTableStatusEnum) {
        val updatedList = reservationTimeSections.value.toMutableList()

        reservationTimeSections.value.find { it.time == reservationTime.time }?.let { updatedTime ->
            val idx = reservationTimeSections.value.indexOf(updatedTime)
            updatedList.set(idx, updatedTime.copy(status = status))
            _reservationTimeSections.value = updatedList
        }
    }

    
    // Firebase에서 예약 정보를 불러와서 상태 업데이트
    fun getReservations() {
        viewModelScope.launch {
            // 데이터 가져오기
            firebaseDataSource.getReservations(
                shopId = salonDetail.value?.shopId ?: -1,
                date = selectedDate.value
            ).collect { disableTimes ->
                if(disableTimes.isNotEmpty()) {
                    _reservationTimeSections.value = reservationTimeSections.value.map {
                        it.copy(
                            status = if(disableTimes.contains(it.time)) TimeTableStatusEnum.Disable else it.status
                            // 가져온 데이터에 따라 특정 시간대를 'Disable'상태로 설정하여 사용자에게 표시
                        )
                    }
                } else {
                    initReservationTimeSections()
                }
            }
        }
    }






    // 정기 예약 날짜 계산
    fun getRecurringDates(startDate: String, weeks: Int): List<String> {
        val recurringDates = mutableListOf<String>()
        val calendar = Calendar.getInstance()

        // 선택된 날짜를 calendar로 변환
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        calendar.time = dateFormat.parse(startDate)

        // 첫 번째 선택한 날짜 추가
        recurringDates.add(dateFormat.format(calendar.time))

        // 주별로 반복되는 예약 날짜 계산 (첫 주 제외하고 나머지 주 계산)
        for (i in 1 until weeks) {
            calendar.add(Calendar.DATE, 7)  // 7일씩 더함
            recurringDates.add(dateFormat.format(calendar.time))
        }

        return recurringDates
    }

    // 정기 예약 서버 전송
    fun postRecurringReservation(recurringDates: List<String>) {
        viewModelScope.launch {
            recurringDates.forEach { date ->
                firebaseDataSource.postReservation(
                    ReservationModel(
                        shopId = salonDetail.value?.shopId ?: -1,
                        userId = preferenceManager.getUserId(),
                        date = date,
                        reservationTimes = reservationTimeSections.value
                            .filter { it.status == TimeTableStatusEnum.Selected }
                            .map { it.time }
                    ),
                    onCompleteListener = { result ->
                        if (!result) {
                            Log.e("Reservation", "정기 예약 실패: $date")
                        }
                    }
                )
            }
        }
    }
    enum class DurationTypeEnum {
        OneMonth,
        ThreeMonths,
        SixMonths
    }


    // 예약 데이터 전송 및 처리
    fun makeRecurringReservation(startDate: String, durationType: DurationTypeEnum) {
        val weeks = when (durationType) {
            DurationTypeEnum.OneMonth -> 4
            DurationTypeEnum.ThreeMonths -> 12
            DurationTypeEnum.SixMonths -> 24
        }

        val recurringDates = getRecurringDates(startDate, weeks)

        // 서버로 예약 전송
        postRecurringReservation(recurringDates) // 직접호출
    }




    
    // 예약 요청 전송 및 결과 처리
    fun reservation(onCompleteListener: (Boolean) -> Unit) {
        // FirebaseDataSource의 postReservation 메서드 호출, 예약 데이터를 서버로 전송
        firebaseDataSource.postReservation(
            ReservationModel(
                shopId = salonDetail.value?.shopId ?: -1,
                userId = preferenceManager.getUserId(),
                date = selectedDate.value,
                reservationTimes = reservationTimeSections.value
                    .filter { it.status == TimeTableStatusEnum.Selected }
                    .map { it.time }
            ),
            onCompleteListener = { result ->
                onCompleteListener(result)
            }
        )
    }
}