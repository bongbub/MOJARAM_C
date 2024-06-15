package com.example.mojaram.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mojaram.data.FirebaseDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
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
            firebaseDataSource.getRecommendations().collect {
                _recommendations.value = it.map {
                    HairSalonListEntity(
                        salonInfo = it,
                        liked = Random.nextBoolean(),
                        likeCount = Random.nextInt(2, 120)
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
                if(it.isNotEmpty()) {
                    resultListener(it)
                }
            }
        }
    }
}