package com.example.mojaram.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mojaram.data.FirebaseDataSource
import com.example.mojaram.map.SalonModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
}