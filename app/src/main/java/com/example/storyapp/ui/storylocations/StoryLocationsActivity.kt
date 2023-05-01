package com.example.storyapp.ui.storylocations

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.data.retrofit.response.ListStoryItem
import com.example.storyapp.databinding.ActivityStoryLocationsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class StoryLocationsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryLocationsBinding
    private lateinit var storyLocationsViewModel: StoryLocationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storyLocationsViewModel = ViewModelProvider(this@StoryLocationsActivity, ViewModelProvider.NewInstanceFactory()).get(
            StoryLocationsViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        storyLocationsViewModel.getAllStories(getToken())
        storyLocationsViewModel.storiesData.observe(this) { storiesData ->
            addStoryLocationsMarker(storiesData)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getMyLocation()
    }

    private fun addStoryLocationsMarker(storiesData : List<ListStoryItem>) {
        storiesData.forEach { story ->
            val latLng = LatLng(story.lat as Double, story.lon as Double)
            mMap.addMarker(MarkerOptions().position(latLng).title(story.name).snippet(story.description))
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getToken() : String {
        val preferences : SharedPreferences = this.getSharedPreferences("user_token", Context.MODE_PRIVATE)
        val token = preferences.getString("TOKEN", null)
        return token.toString()
    }
}