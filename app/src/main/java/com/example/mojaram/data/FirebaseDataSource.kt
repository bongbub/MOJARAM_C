package com.example.mojaram.data

import android.util.Log
import com.example.mojaram.map.SalonModel
import com.example.mojaram.reservation.ReservationModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Random
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getRecommendations(): Flow<List<SalonModel>> = flow {
        emit(
            firestore.collection(COLLECTION_SHOP)
                .whereNotEqualTo(SHOP_IMAGE, null)
                .get()
                .await()
                .documents
                .let { snapshots ->
                    snapshots.map { snapshot ->
                        SalonModel(
                            shopId = snapshot.data?.get(SHOP_ID) as Long,
                            image = snapshot.data?.get(SHOP_IMAGE) as String,
                            shopName = snapshot.data?.get(SHOP_NAME) as String,
                            shopKeyWord = (snapshot.data?.get(SHOP_KEYWORD) as String)
                                .replace(" ", "")
                                .split(","),
                            address = snapshot.data?.get(SHOP_ADDRESS) as String,
                            longitude = snapshot.data?.get(LONGITUDE) as Double?,
                            latitude = snapshot.data?.get(LATITUDE) as Double?,
                            operationTime = snapshot.data?.get(SHOP_TIME) as String
                        )
                    }.shuffled()
                        .take(6)
                }
        )
    }

    fun getAllLocationValidatedSalons(): Flow<List<SalonModel>> = flow {
        emit(
            firestore.collection(COLLECTION_SHOP)
                .whereNotEqualTo(LATITUDE, null)
                .get()
                .await()
                .documents
                .let { snapshots ->
                    snapshots.map { snapshot ->
                        SalonModel(
                            shopId = snapshot.data?.get(SHOP_ID) as Long,
                            image = snapshot.data?.get(SHOP_IMAGE) as String? ?: "",
                            shopName = snapshot.data?.get(SHOP_NAME) as String,
                            shopKeyWord = (snapshot.data?.get(SHOP_KEYWORD) as String)
                                .replace(" ", "")
                                .split(","),
                            address = snapshot.data?.get(SHOP_ADDRESS) as String,
                            longitude = snapshot.data?.get(LONGITUDE) as Double?,
                            latitude = snapshot.data?.get(LATITUDE) as Double?,
                            operationTime = snapshot.data?.get(SHOP_TIME) as String
                        )
                    }
                }
        )
    }

    fun getReservations(shopId: Long, date: String): Flow<List<String>> = flow {
        val reservationTimes = mutableListOf<String>()
        try {
            val snapshot = firestore.collection("shops").document(shopId.toString())
                .collection("reservations")
                .whereEqualTo("date", date)
                .get()
                .await()
            for (document in snapshot.documents) {
                val reservation = document.toObject(ReservationModel::class.java)
                reservation?.reservationTimes?.let { reservationTimes.addAll(it) }
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error getting reservations", e)
        }
        emit(reservationTimes)
    }

//        firestore.collection(COLLECTION_RESERVATION)
//            .whereEqualTo(SHOP_ID_RESERVATION, shopId)
//            .get()
//            .await()
//            .documents
//            .let { snapshots ->
//                val reservationTimes = snapshots.filter { snapshot ->
//                    snapshot.data?.get(DATE) == date
//                }.map {
//                    it.data?.get(RESERVATION_TIMES) as List<String>
//                }
//                emit(reservationTimes.flatten())
//            }


//    fun postReservation(reservation: ReservationModel, onCompleteListener: (Boolean) -> Unit) {
////        firestore.collection(COLLECTION_RESERVATION)
////            .add(reservation)
////            .addOnCompleteListener {
////                onCompleteListener(it.isSuccessful)
////            }

    fun postReservation(reservation: ReservationModel, onCompleteListener: (Boolean) -> Unit) {
        firestore.runTransaction { transaction ->
            val shopRef = firestore.collection("ReservationList").document(reservation.shopId.toString())
            val snapshot = transaction.get(shopRef)

            if (!snapshot.exists()) {
                // 문서가 존재하지 않으면 생성하고 예약 건수를 1로 설정
                transaction.set(shopRef, mapOf("reservationCount" to 1))
            } else {
                // 문서가 존재하면 예약 건수를 업데이트
                val newReservationCount = (snapshot.getLong("reservationCount") ?: 0) + 1
                transaction.update(shopRef, "reservationCount", newReservationCount)
            }

            // 예약 정보를 reservations 컬렉션에 추가
            val reservationRef = shopRef.collection("reservations").document()
            transaction.set(reservationRef, reservation)
        }.addOnSuccessListener {
            onCompleteListener(true)
        }.addOnFailureListener { e ->
            Log.e("FirestoreError", "Reservation failed", e)
            onCompleteListener(false)
        }
    }

    companion object {
        private const val COLLECTION_SHOP = "shop"
        private const val LATITUDE = "shop_lat"
        private const val LONGITUDE = "shop_long"
        private const val SHOP_NAME = "shop_name"
        private const val SHOP_IMAGE = "shop_img"
        private const val SHOP_ADDRESS = "shop_addr"
        private const val SHOP_ID = "shop_id"
        private const val SHOP_KEYWORD = "shop_keyword"
        private const val SHOP_TIME = "shop_time"

        private const val COLLECTION_RESERVATION = "reservatoin"
        private const val SHOP_ID_RESERVATION = "shopId"
        private const val USER_ID = "userId"
        private const val DATE = "date"
        private const val RESERVATION_TIMES = "reservationTimes"
    }

//    companion object {
//        private const val COLLECTION_SHOP = "shop"
//        private const val LATITUDE = "shop_lat"
//        private const val LONGITUDE = "shop_long"
//        private const val SHOP_NAME = "shop_name"
//        private const val SHOP_IMAGE = "shop_img"
//        private const val SHOP_ADDRESS = "shop_addr"
//        private const val SHOP_ID = "shop_id"
//        private const val SHOP_KEYWORD = "shop_keyword"
//        private const val SHOP_TIME = "shop_time"
//
//        private const val COLLECTION_RESERVATION = "reservatoin"
//        private const val SHOP_ID_RESERVATION = "shopId"
//        private const val USER_ID = "userId"
//        private const val DATE = "date"
//        private const val RESERVATION_TIMES = "reservationTimes"
//    }
}