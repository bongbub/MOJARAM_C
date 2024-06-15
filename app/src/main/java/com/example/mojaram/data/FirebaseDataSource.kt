package com.example.mojaram.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.mojaram.map.SalonModel
import com.example.mojaram.reservation.ReservationModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import kotlin.random.Random


class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
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

    fun getUserNickName(userEmail: String): Flow<String> = flow {
        Log.d("FirebaseDataSource", "Fetching nickname for email: $userEmail")
        val userDocument = firestore.collection(COLLECTION_USER_CUSTOMER)
            .whereEqualTo(DOCUMENT_EMAIL, userEmail)
            .get()
            .await()
        val nickname = if (userDocument.documents.isNotEmpty()) {
            val firstDocument = userDocument.documents[0]
            firstDocument.getString(FIELD_NICKNAME) ?: ""
        } else {
            ""
        }

        Log.d("FirebaseDataSource", "Fetched nickname: $nickname")
        emit(nickname)
    }


    fun getReservations(shopId: Long, date: String): Flow<List<String>> = flow{
        firestore.collection(COLLECTION_RESERVATION)
            .whereEqualTo(SHOP_ID_RESERVATION, shopId)
            .get()
            .await()
            .documents
            .let { snapshots ->
                val reservationTimes = snapshots.filter { snapshot ->
                    snapshot.data?.get(DATE) == date
                }.map {
                    it.data?.get(RESERVATION_TIMES) as List<String>
                }
                emit(reservationTimes.flatten())
            }
    }

    fun postReservation(reservation: ReservationModel, onCompleteListener: (Boolean) -> Unit) {
        firestore.collection(COLLECTION_RESERVATION)
            .add(reservation)
            .addOnCompleteListener {
                onCompleteListener(it.isSuccessful)
            }
    }

    fun postImage(imageUri: Uri): Flow<String> = flow {
        kotlin.runCatching {
            var imgRef = firebaseStorage.reference.child("${imageUri.path?.split("/")?.last()}_${Random.nextInt(0,10000)}")
            val uploadTask = imgRef.putFile(imageUri).await()
            if(uploadTask.task.isSuccessful) {
                val downloadUri = imgRef.downloadUrl.await()
                emit(downloadUri.toString())
            } else {
                emit("")
            }
        }.onFailure {
            emit("")
        }
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

        private const val COLLECTION_USER_CUSTOMER = "user_customer"
        private const val DOCUMENT_EMAIL = "userEmail"
        private const val FIELD_NICKNAME = "nickname"
    }
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
