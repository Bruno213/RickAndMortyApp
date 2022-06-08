package com.example.rickandmortyapi.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.rickandmortyapi.R

class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RickAndMortyAPI)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController!!.navigateUp()  || super.onSupportNavigateUp()
    }
}