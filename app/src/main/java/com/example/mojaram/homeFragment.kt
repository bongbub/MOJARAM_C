package com.example.mojaram

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mojaram.ui.login.vrPage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class homeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val banner: LinearLayout = rootView.findViewById(R.id.vrhairEX)
        val gridban: GridLayout = rootView.findViewById(R.id.grid)

        // xml에 있는 요소들
        val viewPager2 : ViewPager2 = rootView.findViewById(R.id.viewpager2)
        val tabLayout : TabLayout = rootView.findViewById(R.id.tablayout)

        viewPager2.apply {
            adapter = MyPageradapter(context as FragmentActivity)
        }

        TabLayoutMediator(tabLayout, viewPager2) {tab, position->
            when(position){
                0->{
                    tab.text="안양동 추천"
                }
                1->{
                    tab.text="스타일 추천"
                }
                else->{
                    tab.text="홈"
                }

            }
        }.attach()

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