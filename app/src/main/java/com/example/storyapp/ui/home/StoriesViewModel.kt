package com.example.storyapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.response.ListStoryItem
import com.example.storyapp.data.retrofit.response.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel : ViewModel() {
    private val _storiesData = MutableLiveData<List<ListStoryItem>>()
    val storiesData : LiveData<List<ListStoryItem>> = _storiesData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = StoriesViewModel::class.java.simpleName
    }

    fun getAllStories(token : String) {
        _isLoading.value = true
        val client = ApiConfig().getApiService().getAllStories( 20)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(call: Call<StoriesResponse>, response: Response<StoriesResponse>) {
                _isLoading.value = false
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
                _isLoading.value = false
                Log.d(TAG, "onFailure : ${t.message}")
            }

        })
    }
}