package com.example.mojaram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class likeFragment : Fragment() {
    companion object{
        const val TAG: String = "찜 로그"

        fun newInstance() : likeFragment {
            return likeFragment()
        }
    }

    // 프래그먼트와 레이아웃 연결
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        Log.d(MeFragment.TAG, "likeFregment - OnCreateView() called")

        val view = inflater.inflate(R.layout.fragment_like, container, false)
        return view
    }

}