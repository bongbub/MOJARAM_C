package com.example.mojaram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginActivity : AppCompatActivity(){


    private lateinit var homeFragment: homeFragment
    private lateinit var likeFragment: likeFragment
    private lateinit var mapFragment: mapFragment
    private lateinit var meFragment: meFragment
    private lateinit var hairFragment: hairFragment


    private lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration


    // 메모리에 올라갔을 때(실행)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 네비게이션 컨트롤러와 앱바 컨피규레이션 초고히
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2)as NavHostFragment
        navController = navHostFragment.navController

        // 앱바 설정 객체
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.meFragment, R.id.hairFragment,
            R.id.likeFragment, R.id.mapFragment))

        setSupportActionBar((findViewById(R.id.toolbar)))

        setupActionBarWithNavController(navController, appBarConfiguration)

        val btn_nav: BottomNavigationView=findViewById(R.id.bottom_navigation)

        btn_nav.setupWithNavController(navController)


    }

}

