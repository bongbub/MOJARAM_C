package com.example.mojaram

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class Reservation : AppCompatActivity() {

    // Firebase Realtime Database 연동을 위한 객체
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        // Firebase RealtimeDatabase 초기화
        mDbRef = Firebase.database.reference

        val cal = Calendar.getInstance()

        val startDate: TextView = findViewById(R.id.startDate)

        val datePickerListener = DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
            startDate.setText("${year}/${month}/${dayOfMonth}")
        }
        startDate.setOnClickListener{
            DatePickerDialog(this, datePickerListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }


        // 시간 선택 다이어로그 만들기
        // 다이어로그 초기화
        val dialogBtn: Button = findViewById(R.id.reservetimebtn)
        val resertime: TextView = findViewById(R.id.resertime)
        dialogBtn.setOnClickListener{
            showDialog(resertime)
        }

        // 예약 버튼을 눌렀을 때 데이터베이스에 정보 저장
        val reserbtn : Button = findViewById(R.id.reservebtn)
        val customer_name : EditText = findViewById(R.id.reserName)
        reserbtn.setOnClickListener{
            // 이름, 날짜, 시간 정보 받아오기
            val reser_day = startDate.text.toString()
            val username = customer_name.text.toString()
            val res_time = resertime.text.toString()

            // 실제 데이터베이스에 저장
            Reservefun(username, reser_day, res_time)
        }


    }
    // 다이어로그 호출
    private fun showDialog(resertime:TextView) {
        // 데이터 담기
        val timedata : Array<String> = resources.getStringArray(R.array.selectreservetime)

        // AlertDialog 초기화
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)

        // 제목 설정
        builder.setTitle("방문 시간")

        // 아이템 선택 이벤트
        AlertDialog.Builder(this).run{
            setItems(timedata,object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    resertime.append("${timedata[which]}")
                }
            })
            setNegativeButton("취소",null)
            show()
        }
    }

    private fun Reservefun (username :String, reser_day:String, res_time:String){

        val reserveData = hashMapOf(
            "username" to username,
            "day" to reser_day,
            "res_time" to res_time
        )
        mDbRef.child("Reserve").child(username).setValue(reserveData)
    }



}