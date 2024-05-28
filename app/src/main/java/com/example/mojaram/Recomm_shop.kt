package com.example.mojaram

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


// 메인페이지 안양동추천 View 매장 정보를 담을 Fragment
class Recomm_shop : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recomm_shop, container, false)

        val rootview = inflater.inflate(R.layout.fragment_recomm_shop, container, false)

        val reserbtn : Button = rootview.findViewById(R.id.reservation_btn)

    }
}