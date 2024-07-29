package com.example.mojaram.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.EventLog
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mojaram.LogInActivity
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivityAdminMainBinding
import com.example.mojaram.databinding.FragmentHairBinding
import com.example.mojaram.map.AdminModel
import com.example.mojaram.model.Reservation
import com.example.mojaram.utils.collectWhenStarted
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.AndroidEntryPoint


class AdminMain : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminAdapter: AdminAdapter
    private lateinit var reservationList: ArrayList<Reservation>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // 로그아웃
    private lateinit var fb: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_main)

        FirebaseApp.initializeApp(this)
        Log.d("AdminMain", "FirebaseApp initialized")

        recyclerView = binding.recyclerviewReservation
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        reservationList = ArrayList()
        adminAdapter = AdminAdapter(reservationList)
        recyclerView.adapter = adminAdapter

        firestore = FirebaseFirestore.getInstance()

        // 로그아웃
        fb = FirebaseAuth.getInstance()
        binding.adminLogoutBtn.setOnClickListener {
            fb.signOut()
            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Log.d("AdminMain", "User logged out")
        }


        // 당겨서 새로고침을 위한 swiperefreshLayout 초기화 및 리스너 생성하기
        swipeRefreshLayout = binding.refreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        EventChangeListener()
    }

    private fun EventChangeListener() {
        firestore = FirebaseFirestore.getInstance()
        Log.d("AdminMain", "Firestore instance initialized")

        firestore.collection("reservation")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firebase Error", error.message.toString())
                    return@addSnapshotListener
                }
                reservationList.clear()

                for (dc in value?.documentChanges!!) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            var reservation = dc.document.toObject(Reservation::class.java)
                            reservation.shopId = dc.document.getLong("shopId") ?: 0
                            reservationList.add(reservation)
                            Log.d("Firestore", "Added reservation: $reservation")
                        }

                        DocumentChange.Type.MODIFIED -> {
                            var reservation = dc.document.toObject(Reservation::class.java)
                            reservation.shopId = dc.document.getLong("shopId") ?: 0
                            // Perform any necessary update logic if needed
                            Log.d("Firestore", "Modified reservation: $reservation")
                        }

                        DocumentChange.Type.REMOVED -> {
                            var reservation = dc.document.toObject(Reservation::class.java)
                            // Perform any necessary removal logic if needed
                            Log.d("Firestore", "Removed reservation: $reservation")
                        }
                    }
                }

                adminAdapter.notifyDataSetChanged()
                // 당겨서 새로고침 리스너
                swipeRefreshLayout.isRefreshing = false
            }
    }
    private fun refreshData(){
        // 당겨서 새로고침 -> 데이터 갱신
        EventChangeListener()
    }


//    private lateinit var recyclerView: RecyclerView
//    private lateinit var userArrayList: ArrayList<Reservation>
//    private lateinit var adminAdapter: AdminAdapter
//    private lateinit var db: FirebaseFirestore
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_admin_main)
//        val binding: ActivityAdminMainBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_admin_main)
//
//        FirebaseApp.initializeApp(this)
//        Log.d("AdminMain", "FirebaseApp initialized")
//
//        recyclerView = findViewById(R.id.recyclerviewReservation)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//
//        userArrayList = arrayListOf()
//
//        adminAdapter = AdminAdapter(userArrayList)
//
//        recyclerView.adapter = adminAdapter
//
//        // Initialize Firestore
//        db = FirebaseFirestore.getInstance()
//        EventChangeListener()
//
//    }
//
//    private fun EventChangeListener() {
//        db = FirebaseFirestore.getInstance()
//        Log.d("AdminMain", "Firestore instance initialized")
//
//        db.collection("reservatoin")
//            .addSnapshotListener { value, error ->
//                if (error != null) {
//                    Log.e("Firebase Error", error.message.toString())
//                    return@addSnapshotListener
//                }
//                userArrayList.clear()
//
//                for (dc in value?.documentChanges!!) {
//                    if (dc.type == DocumentChange.Type.ADDED) {
//                        var reservation = dc.document.toObject(Reservation::class.java)
//                        reservation.shopId = dc.document.getLong("shopId") ?: 0
//                        userArrayList.add(reservation)
//                    }
//                }
//
//                adminAdapter.notifyDataSetChanged()
//            }
//    }
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
