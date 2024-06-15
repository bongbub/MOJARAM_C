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
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val preferenceManager: PreferenceManager
): ViewModel() {
    val timeTables = (10..24)

    private val _reservationTimeSections = MutableStateFlow<List<ReserveTimeEntity>>(listOf())
    val reservationTimeSections = _reservationTimeSections.asStateFlow()

    private val _salonDetail = MutableStateFlow<SalonModel?>(null)
    val salonDetail = _salonDetail.asStateFlow()

    private val _selectedDate = MutableStateFlow<String>(dateFormat.format(Date()))
    val selectedDate = _selectedDate.asStateFlow()

    private val _reservations = MutableStateFlow<List<ReservationModel>>(emptyList())
    val reservations = _reservations.asStateFlow()

    // 유저 닉네임 가져오기
    private val _userNickName = MutableStateFlow<String>("")
    val userNickName = _userNickName.asStateFlow()

    init {
        initReservationTimeSections()
        getReservations()
        getUserNickName()
    }

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
    
    // 유저닉네임 가져오는 함수
    private fun getUserNickName() {
        viewModelScope.launch {
            try {
                val userEmail = preferenceManager.getUserEmail()
                Log.d("ReservationViewModel", "User Email: $userEmail")

                firebaseDataSource.getUserNickName(userEmail).collect { nickname ->
                    _userNickName.value = nickname
                    Log.d("ReservationViewModel", "User Nickname: $nickname")
                }
            } catch (e: Exception) {
                Log.e("ReservationViewModel", "Error fetching user nickname", e)
            }
        }
    }

    fun changeSalonDetail(salonModel: SalonModel) {
        _salonDetail.value = salonModel
    }

    fun changeSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun changeTimeSelected(reservationTime: ReserveTimeEntity, status: TimeTableStatusEnum) {
        val updatedList = reservationTimeSections.value.toMutableList()

        reservationTimeSections.value.find { it.time == reservationTime.time }?.let { updatedTime ->
            val idx = reservationTimeSections.value.indexOf(updatedTime)
            updatedList.set(idx, updatedTime.copy(status = status))
            _reservationTimeSections.value = updatedList
        }
    }
// 또롤백 ㅅㅂ ㄷ체 어케하는건데
    fun getReservations() {
        viewModelScope.launch {
            firebaseDataSource.getReservations(
                shopId = salonDetail.value?.shopId ?: -1,
                date = selectedDate.value
            ).collect { disableTimes ->
                if(disableTimes.isNotEmpty()) {
                    _reservationTimeSections.value = reservationTimeSections.value.map {
                        it.copy(
                            status = if(disableTimes.contains(it.time)) TimeTableStatusEnum.Disable else it.status
                        )
                    }
                } else {
                    initReservationTimeSections()
                }
            }
        }
    }

    fun reservation(onCompleteListener: (Boolean) -> Unit) {
        firebaseDataSource.postReservation(
            ReservationModel(
                shopId = salonDetail.value?.shopId ?: -1,
                userId = preferenceManager.getUserId(),
                date = selectedDate.value,
                reservationTimes = reservationTimeSections.value
                    .filter { it.status == TimeTableStatusEnum.Selected }
                    .map { it.time },
                userNickName = preferenceManager.getUserName()
            ),
            onCompleteListener = { result ->
                onCompleteListener(result)
            }
        )
    }
}