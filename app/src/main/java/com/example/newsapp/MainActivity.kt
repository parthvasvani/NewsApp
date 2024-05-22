package com.example.newsapp

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings.Global.putString
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.internal.cache2.Relay.Companion.edit
import java.util.Locale

const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsFragment = NewsFragment()
        val savedFragment = SavedNewsFragment()
        val settingsFragment = SettingsFragment()

        // Load the SettingsFragment or other initial fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, SettingsFragment())
                .commit()
        }

        setCurrentFragment(newsFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            when (item.itemId) {
                R.id.i_news -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, newsFragment)
                        .commit()
                    true
                }
                R.id.i_save ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, savedFragment)
                        .commit()
                    true
                }
                R.id.i_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, settingsFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    private fun setCurrentFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment,fragment)
            .commit()
    }

}

