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

        // 리사이클러뷰 및 어댑터 초기화하기
        recyclerView = binding.recyclerviewReservation
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        reservationList = ArrayList()
        adminAdapter = AdminAdapter(reservationList)
        recyclerView.adapter = adminAdapter

        firestore = FirebaseFirestore.getInstance()


        // 현재 로그인 유저의 role 가져오기
        fetchUserRole()

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
    }

    private fun fetchUserRole() {
        // 현재 로그인된 사용자의 이메일 가져오기
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        Log.d("AdminMain", "Current User Email: $currentUserEmail")

        if (currentUserEmail != null) {
            // 이메일을 사용하여 user_admin에서 문서 가져오기
            firestore.collection("user_admin").document(currentUserEmail).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userRole = document.getString("role")
                        Log.d("AdminMain", "User role: $userRole")
                        findShopByRole(userRole) // 역할에 따라 매장 찾기
                    } else {
                        Log.d("AdminMain", "User document does not exist")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AdminMain", "Error fetching user role", exception)
                }
        } else {
            Log.d("AdminMain", "Current user is null or not logged in")
        }
    }

    // fetchUserDetails 호출 시 null 체크 추가
    private fun fetchReservations(shopId: Int) {
        firestore.collection("reservation")
            .whereEqualTo("shopId", shopId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                reservationList.clear()
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val reservation = document.toObject(Reservation::class.java)
                        if (reservation != null) {
                            val userEmail = reservation.userEmail // 이 부분 확인
                            Log.d("AdminMain", "User Email from reservation: $userEmail")

                            // userEmail이 null인지 체크하고 null이 아니면 fetchUserDetails 호출
                            if (userEmail != null && userEmail.isNotBlank()) {
                                fetchUserDetails(userEmail, reservation) // userEmail을 안전하게 전달
                            } else {
                                Log.e("AdminMain", "User email is null or blank for reservation")
                            }
                        }
                    }
                } else {
                    Log.d("AdminMain", "No reservations found for shop ID: $shopId")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AdminMain", "Error fetching reservations", exception)
            }
    }

    private fun fetchUserDetails(userEmail: String, reservation: Reservation) {
        Log.d("AdminMain", "Fetching details for user email: $userEmail")
        firestore.collection("user_customer").document(userEmail) // userEmail로 문서 검색
            .get()
            .addOnSuccessListener { userDocument ->
                if (userDocument.exists()) {
                    val nickname = userDocument.getString("nickname") ?: "Unknown"
                    val userGender = userDocument.getString("userGender") ?: "Unknown"

                    // 예약 정보를 사용자 정보와 함께 추가
                    reservation.nickname = nickname // Reservation 클래스에 nickname 필드 추가
                    reservation.userGender = userGender // Reservation 클래스에 userGender 필드 추가

                    // 예약 리스트에 추가
                    reservationList.add(reservation)
                    adminAdapter.notifyDataSetChanged()
                } else {
                    Log.d("AdminMain", "User document does not exist for email: $userEmail")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AdminMain", "Error fetching user details", exception)
            }
    }

    private fun findShopByRole(userRole: String?) {
        if (userRole == null) return

        firestore.collection("shop")
            .whereEqualTo("role", userRole)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val shopIdField = querySnapshot.documents[0].get("shop_id") // shop_id를 가져옴
                    val shopId = when (shopIdField) {
                        is Number -> shopIdField.toInt() // 숫자인 경우 Int로 변환
                        else -> null
                    }

                    if (shopId != null) {
                        Log.d("AdminMain", "Shop ID: $shopId")
                        fetchReservations(shopId) // 예약 정보를 가져오기 위해 shopId를 Int로 전달
                        EventChangeListener(shopId) // EventChangeListener에 shopId 전달
                    } else {
                        Log.e("AdminMain", "shop_id is not a valid type")
                    }
                } else {
                    Log.d("AdminMain", "No shops found for role: $userRole")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AdminMain", "Error fetching shop", exception)
            }
    }

    private fun EventChangeListener(shopId: Number) {
        firestore.collection("reservation")
            .whereEqualTo("shopId", shopId) // shopId가 일치하는 예약만 가져옴
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firebase Error", error.message.toString())
                    return@addSnapshotListener
                }
                reservationList.clear()

                value?.documentChanges?.forEach { dc ->
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val reservation = dc.document.toObject(Reservation::class.java)
                            fetchUserDetails(reservation.userId, reservation) // 사용자 정보를 가져오고 예약 정보에 추가
                            Log.d("AdminMain", "Added reservation: $reservation")
                        }
                        DocumentChange.Type.MODIFIED -> {
                            // 예약 수정 로직 추가
                            Log.d("AdminMain", "Modified reservation: ${dc.document.id}")
                        }
                        DocumentChange.Type.REMOVED -> {
                            // 예약 삭제 로직 추가
                            Log.d("AdminMain", "Removed reservation: ${dc.document.id}")
                        }
                    }
                }

                // 어댑터에 데이터 변경 통지
                adminAdapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
    }

    private fun refreshData() {
        // 당겨서 새로고침 -> 데이터 갱신
        fetchUserRole() // 역할에 따라 매장 찾기
    }

}