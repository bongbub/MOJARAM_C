package com.example.mojaram.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mojaram.databinding.FragmentMyPageBinding
import com.example.mojaram.utils.AutoClearedValue

class MyPageFragment: Fragment() {
    private var binding by AutoClearedValue<FragmentMyPageBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMyPageBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navToProfileSetting()
    }

    private fun navToProfileSetting() {
        binding.cardviewMyInfo.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileSettingActivity::class.java))
        }
    }
}