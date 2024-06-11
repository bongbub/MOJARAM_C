package com.example.mojaram.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mojaram.R
import com.example.mojaram.data.FirebaseDataSource
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): ViewModel() {

    private val _salons = MutableStateFlow<List<SalonModel>>(listOf())
    val salons = _salons.asStateFlow();

    private val _markers = MutableStateFlow<List<Marker>>(listOf())
    val markers = _markers.asStateFlow()

    private val _selectedSalon = MutableStateFlow<SalonModel?>(null)
    val selectedSalon = _selectedSalon.asStateFlow()

    fun getShopData(naverMap: NaverMap) {
        viewModelScope.launch {
            firebaseDataSource.getAllLocationValidatedSalons().collect {
                _salons.value = it
                it.map { salon ->
                    val marker = Marker(LatLng(salon.latitude ?: 0.0, salon.longitude ?: 0.0))
                    marker.map = naverMap
                    marker.icon = OverlayImage.fromResource(R.drawable.salon_marker)
                    marker.onClickListener = Overlay.OnClickListener {
                        _selectedSalon.value = salon
                        true
                    }
                    marker
                }.let { createdMarkers ->
                    _markers.value = createdMarkers
                }
            }
        }
    }
}