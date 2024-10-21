package com.example.mojaram

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mojaram.databinding.FragmentHairBinding
import com.example.mojaram.home.HairSalonListAdapter
import com.example.mojaram.home.HomeViewModel
import com.example.mojaram.map.MapFragment
import com.example.mojaram.salon.SalonDetailActivity
import com.example.mojaram.utils.AutoClearedValue
import com.example.mojaram.utils.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HairFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentHairBinding>()
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHairBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setShopRecommendationList()
    }

    private fun setShopRecommendationList() {
        binding.recyclerviewSalons.adapter = HairSalonListAdapter(
            onClickItem = { salonModel ->
                Intent(requireContext(), SalonDetailActivity::class.java).let {
                    it.putExtra(
                        MapFragment.SALON_DETAIL_KEY,
                        salonModel
                    )
                    startActivity(it)
                }
            },
            onLikeClick = { hairSalonListEntity, isLiked ->
                if (isLiked) {
                    viewModel.likeSalon(hairSalonListEntity.salonInfo)
                } else {
                    viewModel.unlikeSalon(hairSalonListEntity.salonInfo)
                }
            }
        )

        collectWhenStarted(viewModel.recommendations) { recommendations ->
            (binding.recyclerviewSalons.adapter as HairSalonListAdapter).submitList(recommendations)
        }
    }

    companion object {
        const val TAG: String = "상품페이지 로그"

        fun newInstance(): HairFragment {
            return HairFragment()
        }
    }
}
