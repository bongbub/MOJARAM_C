package com.example.mojaram

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2

class likeFragment : Fragment() {
    companion object {
        const val TAG: String = "찜 로그"

        fun newInstance(): likeFragment {
            return likeFragment()
        }
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "LikeFragment - onCreateView() called")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_like, container, false)

        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.likeTablyout)

        // Set up ViewPager2 with TabLayout
        val pagerAdapter = LikePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "찜한 매장"
                1 -> "찜한 스타일"
                else -> null
            }
        }.attach()

        return view
    }

    private inner class LikePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> fragment1()
                1 -> fragment2()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }
}
