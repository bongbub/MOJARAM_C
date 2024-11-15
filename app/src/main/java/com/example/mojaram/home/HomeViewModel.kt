package com.example.mojaram.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mojaram.data.FirebaseDataSource
import com.example.mojaram.map.SalonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {

    init {
        getRecommendations()
    }

    private val _recommendations = MutableStateFlow<List<HairSalonListEntity>>(listOf())
    val recommendations = _recommendations.asStateFlow()

    private val _onLoading = MutableStateFlow<Boolean>(false)
    val onLoading = _onLoading.asStateFlow()

    private fun getRecommendations() {
        viewModelScope.launch {
            firebaseDataSource.getRecommendations().collect { salons ->
                val userId = firebaseAuth.currentUser?.email // 현재 사용자 ID 가져오기
                val likedShops = firestore.collection("user_customer")
                    .document(userId ?: "")
                    .get()
                    .await()
                    .get("likedShops") as? List<Long> ?: emptyList() // 찜한 매장 ID 목록 가져오기

                _recommendations.value = salons.map { salon ->
                    val likeCount = firestore.collection("shop")
                        .document(salon.shopId.toString())
                        .get()
                        .await()
                        .getLong("likeCount") ?: 0 // 실제 찜 카운트 가져오기

                    HairSalonListEntity(
                        salonInfo = salon,
                        liked = likedShops.contains(salon.shopId), // 찜한 매장인지 확인
                        likeCount = likeCount.toInt() // Firestore에서 가져온 찜 카운트
                    )
                }
            }
        }
    }

    fun uploadImage(imageUri: Uri, resultListener: (String) -> Unit) {
        viewModelScope.launch {
            _onLoading.value = true
            firebaseDataSource.postImage(imageUri).collect {
                _onLoading.value = false
                if (it.isNotEmpty()) {
                    resultListener(it)
                }
            }
        }
    }

    fun likeSalon(salon: SalonModel) {
        val userId = firebaseAuth.currentUser?.email ?: return

        // Firestore에서 사용자 컬렉션의 likedShops에 shop_id 추가
        firestore.collection("user_customer").document(userId)
            .update("likedShops", FieldValue.arrayUnion(salon.shopId))
            .addOnSuccessListener {
                // 매장 컬렉션에서 likeCount 증가
                firestore.collection("shop").document(salon.shopId.toString())
                    .update("likeCount", FieldValue.increment(1))
                    .addOnSuccessListener {
                        // 찜 카운트 업데이트
                        updateLikeCount(salon.shopId, true)
                    }
            }
    }

    fun unlikeSalon(salon: SalonModel) {
        val userId = firebaseAuth.currentUser?.email ?: return

        // Firestore에서 사용자 컬렉션의 likedShops에서 shop_id 제거
        firestore.collection("user_customer").document(userId)
            .update("likedShops", FieldValue.arrayRemove(salon.shopId)).addOnSuccessListener {
                // 매장 컬렉션에서 likeCount 감소
                firestore.collection("shop").document(salon.shopId.toString())
                    .update("likeCount", FieldValue.increment(-1))
                    .addOnSuccessListener {
                        // 찜 카운트 업데이트
                        updateLikeCount(salon.shopId, false)
                    }
            }
    }

    // 찜 카운트 업데이트 메서드
    private fun updateLikeCount(shopId: Long, isLiked: Boolean) {
        _recommendations.value = _recommendations.value.map { entity ->
            if (entity.salonInfo.shopId == shopId) {
                entity.copy(
                    likeCount = if (isLiked) entity.likeCount + 1 else entity.likeCount - 1
                )
            } else {
                entity
            }
        }

    }
}