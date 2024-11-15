package com.example.mojaram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mojaram.home.HomeFragment
import com.example.mojaram.map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    private lateinit var navController: NavController


    // 메모리에 올라갔을 때(실행)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)as NavHostFragment
        navController = navHostFragment.navController

        val btn_nav: BottomNavigationView=findViewById(R.id.bottom_navigation)

        btn_nav.setupWithNavController(navController)
        btn_nav.itemIconTintList = null

    }

}

