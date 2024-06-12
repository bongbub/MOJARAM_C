package com.example.mojaram.map

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.mojaram.R
import com.example.mojaram.databinding.FragmentMapBinding
import com.example.mojaram.salon.SalonDetailActivity
import com.example.mojaram.utils.AutoClearedValue
import com.example.mojaram.utils.collectWhenStarted
import com.example.mojaram.utils.showToast
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private var binding by AutoClearedValue<FragmentMapBinding>()
    private val viewModel by viewModels<MapViewModel>()

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if(permissions.all { it.value }) {
            initMapView()
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
        locationPermissionLauncher.launch(LOCATION_PERMISSIONS)
        it.root
    }

    override fun onMapReady(map: NaverMap) {
        this.naverMap = map
        naverMap.locationSource = locationSource //현재 위치
        naverMap.uiSettings.isLocationButtonEnabled = true //현재 위치 버튼 기능
        naverMap.locationTrackingMode = LocationTrackingMode.Follow // 위치 추적하면서 카메라도 움직임
        naverMap.setOnMapClickListener { point, coord ->
            viewModel.changeSelectedSalon(null)
        }
        viewModel.getShopData(naverMap)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateSalons()
        navToSalonDetail()
    }

    private fun initMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_container) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_container, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun updateSalons() {
        collectWhenStarted(viewModel.selectedSalon) { salon ->
            if(salon != null) {
                binding.textviewSalonName.text = salon.shopName
                binding.textviewSalonTags.text = salon.shopKeyWord.map { "#${it}" }.joinToString("  ")
                binding.textviewOperationTime.text = salon.operationTime
                binding.textviewSalonAddress.text = salon.address
                binding.layoutSelectedSalon.visibility = View.VISIBLE
                binding.imageviewSalon.load(salon.image)
            } else {
                binding.layoutSelectedSalon.visibility = View.GONE
            }
        }
    }

    private fun navToSalonDetail() {
        binding.layoutSelectedSalon.setOnClickListener {
            Intent(requireContext(), SalonDetailActivity::class.java).let {
                it.putExtra(SALON_DETAIL_KEY, viewModel.selectedSalon.value)
                startActivity(it)
            }
        }
    }


    companion object {
        const val SALON_DETAIL_KEY = "SALON_DETAIL_KEY"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 5000
        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}