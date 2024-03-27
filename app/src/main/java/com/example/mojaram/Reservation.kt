package com.example.mojaram

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import java.util.Calendar

class Reservation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        val cal = Calendar.getInstance()

        val startDate: EditText = findViewById(R.id.startDate)

        val datePickerListener = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
            startDate.setText("${year}/${month}/${dayOfMonth}")
        }
        startDate.setOnClickListener{
            DatePickerDialog(this, datePickerListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

    }
}