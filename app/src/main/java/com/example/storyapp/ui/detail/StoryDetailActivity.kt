package com.example.storyapp.ui.detail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.retrofit.response.DetailStory
import com.example.storyapp.databinding.ActivityStoryDetailBinding
import com.example.storyapp.ui.home.StoriesViewModel

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStoryDetailBinding
    private lateinit var detailViewModel: StoryDetailViewModel
    private var id : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Detail Story"

        id = intent.getStringExtra("id")

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            StoryDetailViewModel::class.java)
        detailViewModel.getStoryDetail(id ?: "",getToken())
        detailViewModel.storyData.observe(this) { storyData ->
            setStoryData(storyData)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun getToken() : String {
        val preferences : SharedPreferences = this.getSharedPreferences("user_token", Context.MODE_PRIVATE)
        val token = preferences.getString("TOKEN", null)
        return token.toString()
    }

    private fun setStoryData(storyData : DetailStory) {
        binding.tvDetailDescription.text = storyData.description
        binding.tvDetailName.text = storyData.name
        Glide.with(this).load(storyData.photoUrl).into(binding.ivDetailPhoto)
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}