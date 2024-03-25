package com.example.mojaram

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


// 메인페이지 스타일 추천 View 스타일 정보를 담을 Fragment
class Recomm_style : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recomm_style, container, false)

        val rootView = inflater.inflate(R.layout.fragment_recomm_style, container, false)

        val styleImg : ImageView = rootView.findViewById(R.id.styleImage)
        val styleName: TextView = rootView.findViewById(R.id.styleName)
        val styleDes : TextView = rootView.findViewById(R.id.shop_description)
        val reserveBtn : Button = rootView.findViewById(R.id.reservation_btn)

        // 예약 페이지로 이동
        reserveBtn.setOnClickListener{
            //val intent = Intent(requireContext(),)
        }

    }

}