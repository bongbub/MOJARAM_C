package com.example.mojaram.reservation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.GridLayout
import android.widget.GridLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityReservationBinding
import com.example.mojaram.databinding.ItemTimeTablesBinding
import com.google.firebase.firestore.FirebaseFirestore

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private val timeTables = (10..24)
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackButton()
        setDateSelectListener()
        setTimeTables()
        setConfirmButton()
    }

    private fun setBackButton() {
        binding.imageviewBack.setOnClickListener {
            finish()
        }
    }

    private fun setDateSelectListener() {
        binding.calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            Log.d("ReservationActivity", "Selected date: $selectedDate")
        }
    }


    private fun setTimeTables() {
        binding.gridlayoutTimeTables.run {
            columnCount = 5
            timeTables.forEachIndexed { _, time ->
                val reserveTimeEntity = ReserveTimeEntity(
                    time = if (time <= 21) "$time:00" else "",
                    status = if (time > 21) TimeTableStatusEnum.None else TimeTableStatusEnum.Available
                )

                var item = ItemTimeTablesBinding.inflate(layoutInflater, binding.gridlayoutTimeTables, false)
                val params = LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
                )
                params.setGravity(Gravity.FILL)
                item.root.layoutParams = params
                item.reserveTimeEntity = reserveTimeEntity
                item.setTimeItemBackground(reserveTimeEntity.status)
                item.root.setOnClickListener {
                    val status = (item.reserveTimeEntity as ReserveTimeEntity).status
                    if (status != TimeTableStatusEnum.Disable && status != TimeTableStatusEnum.None) {
                        if (status == TimeTableStatusEnum.Available) {
                            item.setTimeItemBackground(TimeTableStatusEnum.Selected)
                            selectedTime = item.reserveTimeEntity?.time
                            Log.d("ReservationActivity", "Selected time: $selectedTime")
                        } else {
                            item.setTimeItemBackground(TimeTableStatusEnum.Available)
                            selectedTime = null
                            Log.d("ReservationActivity", "Time deselected")
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
    private fun setConfirmButton() {
        binding.btnConfirmReservation.setOnClickListener {
            if (selectedDate != null && selectedTime != null) {
                Log.d("ReservationActivity", "Date and time selected: $selectedDate $selectedTime")
                saveReservationToFirestore(selectedDate!!, selectedTime!!)
                val intent = Intent(this, ReservationConfirmationActivity::class.java)
                intent.putExtra("DATE", selectedDate)
                intent.putExtra("TIME", selectedTime)
                startActivity(intent)
            } else {
                Log.d("ReservationActivity", "Date or time not selected")
            }
        }
    }

    private fun saveReservationToFirestore(date: String, time: String) {
        val db = FirebaseFirestore.getInstance()
        val reservation = hashMapOf(
            "date" to date,
            "time" to time,
            "user" to "사용자 정보"  // 실제 사용자 정보로 변경해야 함
        )
        db.collection("reservations")
            .add(reservation)
            .addOnSuccessListener { documentReference ->
                Log.d("ReservationActivity", "Reservation added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("ReservationActivity", "Error adding reservation", e)
            }
    }
}