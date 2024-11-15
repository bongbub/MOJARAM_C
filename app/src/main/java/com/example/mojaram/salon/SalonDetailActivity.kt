package com.example.mojaram.salon

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.mojaram.R
import com.example.mojaram.databinding.ActivitySalonDetailBinding
import com.example.mojaram.map.MapFragment
import com.example.mojaram.map.SalonModel
import com.example.mojaram.reservation.ReservationActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs

class SalonDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySalonDetailBinding
    private var salonDetail: SalonModel? = null
    private val firestore = FirebaseFirestore.getInstance()
    private var isLiked: Boolean = false // 찜 상태 변수
    private var likeCount: Int = 0 // 찜 카운트 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSalonData()
        listenScrollState()
        clickReserve()
        loadLikeData()  // 찜상태와 갯수 불러오깅
    }

    private fun loadLikeData() {
        salonDetail?.let { salon ->
            firestore.collection("salons").document(salon.shopId.toString()).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        isLiked = document.getBoolean("liked") ?: false
                        likeCount = document.getLong("likeCount")?.toInt() ?: 0
                        //updateLikeUI()
                    }
                }
        }
    }
//    private fun updateLikeUI() {
//        val buttonLikeBinding = binding.buttonLike // button_salon_detail 레이아웃의 바인딩 객체 생성
//
//        buttonLikeBinding.checkboxLike.isChecked = isLiked
//        buttonLikeBinding.textviewLikeCount.text = likeCount.toString()
//
//        buttonLikeBinding.checkboxLike.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                likeCount += 1
//                firestore.collection("salons").document(salonDetail!!.shopId.toString())
//                    .update("likeCount", likeCount, "liked", true)
//            } else {
//                likeCount -= 1
//                firestore.collection("salons").document(salonDetail!!.shopId.toString())
//                    .update("likeCount", likeCount, "liked", false)
//            }
//            buttonLikeBinding.textviewLikeCount.text = likeCount.toString()
//        }
//    }

    private fun listenScrollState() {
        binding.appbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                setBackButton(true)
                setSalonNameVisibility(true)
            } else if (verticalOffset == 0) {
                setBackButton(false)
                setSalonNameVisibility(false)
            }
        }
    }

    private fun setBackButton(collapse: Boolean) {
        binding.imageviewBack.run {
            setImageResource(
                if(collapse) R.drawable.ic_back_black else R.drawable.ic_back_white
            )
            setOnClickListener {
                finish()
            }
        }
    }

    private fun setSalonNameVisibility(collapse: Boolean) {
        binding.textviewSalonNameCollapse.visibility = if(collapse) View.VISIBLE else View.GONE
    }

    private fun clickReserve() {
        binding.textviewReservation.setOnClickListener {
            Intent(this, ReservationActivity::class.java).let {
                it.putExtra(MapFragment.SALON_DETAIL_KEY, salonDetail)
                startActivity(it)
            }

        }
    }

    private fun loadSalonData() {
        intent.getParcelableExtra(
            MapFragment.SALON_DETAIL_KEY,
            SalonModel::class.java
        )?.let { salon ->
            salonDetail = salon
            binding.textviewSalonName.text = salon.shopName
            binding.textviewSalonNameCollapse.text = salon.shopName
            binding.textviewWorkTimeValue.text = salon.operationTime
            binding.textviewInfoValue.text = salon.address
            binding.imageviewSalon.load(salon.image)
        }
    }
}