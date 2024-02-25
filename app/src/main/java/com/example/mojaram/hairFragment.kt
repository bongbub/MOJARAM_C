package com.example.mojaram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class hairFragment : Fragment() {

    companion object{
        const val TAG: String = "상품페이지 로그"

        fun newInstance() : hairFragment {
            return hairFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        Log.d(meFragment.TAG, "HairFregment - OnCreateView() called")

        val view = inflater.inflate(R.layout.fragment_hair, container, false)
        return view
    }
}