package com.example.storyapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.authentication.LoginFragment
import com.example.storyapp.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginFragment = LoginFragment()
        val homeFragment = HomeFragment()
        val fragmentManager = supportFragmentManager


        if (getToken() == null) {
            fragmentManager.beginTransaction().apply {
                replace(R.id.authenticationFragment, loginFragment, LoginFragment::class.java.simpleName)
                commit()
            }
        } else {
            fragmentManager.beginTransaction().apply {
                replace(R.id.authenticationFragment, homeFragment, HomeFragment::class.java.simpleName)
                commit()
            }
        }
    }

    private fun getToken(): String? {
        val preferences: SharedPreferences = this.getSharedPreferences("user_token", Context.MODE_PRIVATE)
        return preferences.getString("TOKEN", null)
    }
}