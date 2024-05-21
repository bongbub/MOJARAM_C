package com.example.mojaram

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageradapter(fa: FragmentActivity):FragmentStateAdapter(fa) {
    private val NUM_PAGES = 2

    override fun getItemCount() : Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> view01()
            1 -> view02()
            else -> HomeFragment()
        }
    }
}