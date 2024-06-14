package com.example.mojaram.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.EventLog
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityAdminMainBinding
import com.example.mojaram.databinding.FragmentHairBinding
import com.example.mojaram.map.AdminModel
import com.example.mojaram.model.Reservation
import com.example.mojaram.utils.collectWhenStarted
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.AndroidEntryPoint


class AdminMain : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Reservation>
    private lateinit var adminAdapter: AdminAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_admin_main)
        val binding: ActivityAdminMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_admin_main)

        FirebaseApp.initializeApp(this)
        Log.d("AdminMain", "FirebaseApp initialized")

        recyclerView = findViewById(R.id.recyclerviewReservation)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        adminAdapter = AdminAdapter(userArrayList)

        recyclerView.adapter = adminAdapter

        EventChangeListener()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        Log.d("AdminMain", "Firestore instance initialized")

        db.collection("reservation")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firebase Error", error.message.toString())
                    return@addSnapshotListener
                }

                for (dc in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        var reservation = dc.document.toObject(Reservation::class.java)
                        // shopId를 Long으로 변환하여 String으로 할당

                        reservation.shopId = dc.document.getLong("shopId")?.toString() ?: ""
                        userArrayList.add(reservation)
                    }
                }

                adminAdapter.notifyDataSetChanged()
            }
    }
}
//        db.collection("reservatoin")
//            .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                override fun onEvent(
//                    value: QuerySnapshot?,
//                    error: FirebaseFirestoreException?
//                ) {
//                    if (error != null) {
//                        Log.e("AdminMainFirebase Error", error.message.toString())
//                        return
//                    }
//
//                    if (value != null) {
//                        for (dc: DocumentChange in value.documentChanges) {
//                            if (dc.type == DocumentChange.Type.ADDED) {
//                                userArrayList.add(dc.document.toObject(Reservation::class.java))
//
//                            }
//                        }
//                        Log.d("AdminMain", "Data fetched: ${userArrayList.size} items")
//                        adminAdapter.notifyDataSetChanged()
//                    } else {
//                        Log.d("AdminMain", "No data received")
//                    }
//                }
//            })




//@AndroidEntryPoint
//class AdminMain : AppCompatActivity() {
//    private lateinit var binding: ActivityAdminMainBinding
//    private val viewModel by viewModels<AdminViewModel>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d("AdminMain", "onCreate called")
//
//        binding = ActivityAdminMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.recyclerviewReservation.layoutManager = LinearLayoutManager(this)
//        val adapter = AdminAdapter()
//        binding.recyclerviewReservation.adapter = adapter
//
//        viewModel.reservations.observe(this) { reservations ->
//            Log.d("AdminMain", "Reservations received: $reservations")
//            adapter.submitList(reservations)
//        }
//    }
//}
