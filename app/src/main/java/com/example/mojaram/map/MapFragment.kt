package com.example.mojaram.map

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.mojaram.databinding.FragmentMapBinding
import com.example.mojaram.utils.AutoClearedValue
import com.example.mojaram.utils.showToast
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class MapFragment : Fragment(), OnMapReadyCallback {
    private var binding by AutoClearedValue<FragmentMapBinding>()

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if(permissions.all { it.value }) {
            (binding.mapContainer as MapFragment).getMapAsync(this)
        } else {
            requireContext().showToast("지도 기능을 사용하려면 위치 권한을 허용해주세요.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMapBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    private fun initMap() {
        locationPermissionLauncher.launch(LOCATION_PERMISSIONS)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 5000
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onMapReady(p0: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource //현재 위치
        naverMap.uiSettings.isLocationButtonEnabled = true //현재 위치 버튼 기능
        naverMap.locationTrackingMode = LocationTrackingMode.Follow // 위치 추적하면서 카메라도 움직임

        naverMap.setOnMapClickListener { point, coord ->

        }
    }
}