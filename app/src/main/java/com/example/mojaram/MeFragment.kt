package com.example.mojaram

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.example.mojaram.databinding.FragmentMeBinding
import com.example.mojaram.utils.AutoClearedValue

class MeFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentMeBinding>()
    companion object {
        const val TAG: String = "마이페이지 로그"

        /*fun newInstance(): MeFragment {
            return MeFragment()
        }*/
    }

    // 프래그먼트와 레이아웃 연결
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "MeFregment - OnCreateView() called")

        binding = FragmentMeBinding.inflate(inflater, container, false)
        return binding.root
        //val view = inflater.inflate(R.layout.fragment_me, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "MeFregment - onViewCreated() called")

        val navController = findNavController(view)
    }



}
