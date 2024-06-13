package com.example.mojaram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class HairFragment : Fragment() {

    companion object{
        const val TAG: String = "상품페이지 로그"

        fun newInstance() : HairFragment {
            return HairFragment()
        }
    }

    private lateinit var listView: ListView
    private lateinit var adapter: ShopListAdapter
    private lateinit var shopList: MutableList<ShopListData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        Log.d(MeFragment.TAG, "HairFregment - OnCreateView() called")

        val view = inflater.inflate(R.layout.fragment_hair, container, false)

        // ListView 초기화
        listView = view.findViewById(R.id.shoplistView)

        // 샘플 데이터 생성
        shopList = arrayListOf(
            ShopListData("모터치 노원", "서울 노원구 동일로 218길 29 우진빌딩", "#가발 #붙임머리", ContextCompat.getDrawable(requireContext(), R.drawable.shop_image1)!!),
            ShopListData("위그온 디자이너가발 노원점", "서울 노원구 노해로 457 6층", "#가발 #맞춤가발 #항암가발 #여자 #남자", ContextCompat.getDrawable(requireContext(), R.drawable.shop_image2)!!),
            ShopListData("박승철위그스투디오 강북노원점", "서울 노원구 동일로 1417 3층", "#가발 #맞춤가발 #항암가발 #여자 #남자", ContextCompat.getDrawable(requireContext(), R.drawable.shop_image3)!!),
            ShopListData("부부가발", "서울 도봉구 마들로 11길 73 103호", "#가발 #기성가발 #맞춤가발", ContextCompat.getDrawable(requireContext(), R.drawable.shop_image4)!!),
            ShopListData("모웰 상봉점", "서울 중랑구 망우로 316 이지팰리스 402호", "#가발", ContextCompat.getDrawable(requireContext(), R.drawable.shop_image5)!!),
            ShopListData("다온모", "서울 중랑구 면목로94길 9 203호", "#가발 #맞춤가발 #남자", ContextCompat.getDrawable(requireContext(), R.drawable.shop_image6)!!),
            ShopListData("가발백화점", "서울 동대문구 왕산로 238 1층", "#가발 #맞춤가발 #타사가발관리 #가발관리 #당일착용 #여자 #남자", ContextCompat.getDrawable(requireContext(), R.drawable.shop_image7)!!),
            // 필요한 만큼 더 추가
        )

        // 어댑터 초기화 및 ListView에 설정
        adapter = ShopListAdapter(requireContext(), shopList as ArrayList<ShopListData>)
        listView.adapter = adapter

        return view


    }
}