package com.example.mojaram

import androidx.fragment.app.Fragment


class homeFragment : Fragment() {
    companion object{
        const val TAG: String = "홈 로그"

        fun newInstance() : homeFragment {
            return homeFragment()
        }
    }

}