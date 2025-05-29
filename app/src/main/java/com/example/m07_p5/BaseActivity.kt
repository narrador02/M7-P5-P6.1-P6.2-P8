package com.example.m07_p5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    protected fun setupBottomNavigation(bottomNavId: Int, selectedItemId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(bottomNavId)
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> {
                        if (this !is MainActivity) navigateTo(MainActivity::class.java)
                        return@setOnItemSelectedListener true
                    }
                    R.id.nav_list -> {
                        if (this !is ListActivity) navigateTo(ListActivity::class.java)
                        return@setOnItemSelectedListener true
                    }
                    R.id.nav_settings -> {
                        if (this !is PreferencesActivity) navigateTo(PreferencesActivity::class.java)
                        return@setOnItemSelectedListener true
                    }
                }
                false
            }
            bottomNav.selectedItemId = selectedItemId
        }
    }

    private fun navigateTo(destination: Class<*>) {
        if (this::class.java != destination) {
            Log.d("BottomNav", "Navegando a ${destination.simpleName}")
            val intent = Intent(this, destination)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
