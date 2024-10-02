package com.example.mojaram.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.mojaram.data.FirebaseDataSource
import com.example.mojaram.model.Reservation
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class AdminViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _reservations = MutableLiveData<List<Reservation>>()
    val reservations: LiveData<List<Reservation>> = _reservations

    init {
        Log.d("AdminViewModel", "AdminViewModel initialized")
        getReservations()
    }

    private fun getReservations() {
        viewModelScope.launch {
            Log.d("AdminViewModel", "getReservations called")
            firestore.collection("reservation").get().addOnSuccessListener { result ->
                val reservations = result.map { document ->
                    val reservation = document.toObject(Reservation::class.java)
                    Log.d("AdminViewModel", "Fetched reservation: $reservation")
                    reservation
                }
                _reservations.value = reservations
                Log.d("AdminViewModel", "Reservations updated: $reservations")
            }.addOnFailureListener { exception ->
                Log.e("AdminViewModel", "Error getting reservations", exception)
            }
        }
    }
}
