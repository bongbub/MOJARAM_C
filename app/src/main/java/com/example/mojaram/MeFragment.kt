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
        /*
        // 리뷰 내역
        binding.btnMy1.setOnClickListener {
            navController.navigate(R.id.action_me_to_myreviews)
        }

        // 예약 내역
        binding.btnMy2.setOnClickListener {
            val intent = Intent(requireContext(), ReservationHistory::class.java)
            startActivity(intent)
        }

        // 상담 내역
        binding.btnMy3.setOnClickListener {
            val intent = Intent(requireContext(), consultationHistory::class.java)
            startActivity(intent)
        }

        // 찜 목록 페이지
        binding.btnMy4.setOnClickListener {
            val intent = Intent(requireContext(), Bookmark::class.java)
            startActivity(intent)
        }

        // 환경 설정 페이지
        binding.btnMy5.setOnClickListener {
            val intent = Intent(requireContext(), settingsPage::class.java)
            startActivity(intent)

        }*/
    }



}
