package com.example.mojaram

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.mojaram.ui.login.vrPage


class homeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val banner: LinearLayout = rootView.findViewById(R.id.vrhairEX)
        val gridban: GridLayout = rootView.findViewById(R.id.grid)

        banner.setOnClickListener {
            val intent = Intent(requireContext(), vrPage::class.java)
            startActivity(intent)
        }
        gridban.setOnClickListener {
            val intent = Intent(requireContext(), vrPage::class.java)
            startActivity(intent)

        }
        return rootView
    }

        companion object {
            const val TAG: String = "홈 로그"

            fun newInstance(): homeFragment {
                return homeFragment()
            }
        }

}