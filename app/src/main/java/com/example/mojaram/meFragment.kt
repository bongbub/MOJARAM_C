package com.example.mojaram

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mojaram.databinding.FragmentMyReviewsBinding

class meFragment : Fragment() {

    companion object{
        const val TAG: String = "마이페이지 로그"

        fun newInstance() : meFragment {
            return meFragment()
        }
    }


    // 프래그먼트와 레이아웃 연결
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        Log.d(TAG, "MeFregment - OnCreateView() called")

        val view = inflater.inflate(R.layout.fragment_me, container, false)
        return view

        val myreviewPage : Button = view.findViewById(R.id.btn_my1)

        myreviewPage.setOnClickListener{
            val intent = Intent(requireContext(), FragmentMyReviewsBinding)
        }
    }
}

