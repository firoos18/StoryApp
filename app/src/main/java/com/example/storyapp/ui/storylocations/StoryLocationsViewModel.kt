package com.example.storyapp.ui.storylocations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.response.ListStoryItem
import com.example.storyapp.data.retrofit.response.StoriesResponse
import com.example.storyapp.ui.home.StoriesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryLocationsViewModel : ViewModel() {
    private val _storiesData = MutableLiveData<List<ListStoryItem>>()
    val storiesData : LiveData<List<ListStoryItem>> = _storiesData

    companion object {
        private val TAG = StoriesViewModel::class.java.simpleName
    }

    fun getAllStories(token : String) {
        val client = ApiConfig().getApiService().getAllStoriesWithLocation(1)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(call: Call<StoriesResponse>, response: Response<StoriesResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _storiesData.value = responseBody.listStory
                    }
                } else {
                    Log.d(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.d(TAG, "onFailure : ${t.message}")
            }

        })
    }
}