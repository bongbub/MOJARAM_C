package com.example.mojaram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class MyReviews : Fragment() {

    // 기본 생성자 대신에 newInstance 메서드를 사용하여 프래그먼트 인스턴스 생성
    companion object {
        fun newInstance(): MyReviews {
            return MyReviews()
        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_reviews, container, false)
    }

}