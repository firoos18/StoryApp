package com.example.storyapp.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.data.retrofit.response.DetailStory
import com.example.storyapp.data.retrofit.response.DetailStoryResponse
import com.example.storyapp.ui.home.StoriesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryDetailViewModel : ViewModel() {
    private val _storyData = MutableLiveData<DetailStory>()
    val storyData : LiveData<DetailStory> = _storyData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = StoriesViewModel::class.java.simpleName
    }

    fun getStoryDetail(id : String, token : String) {
        _isLoading.value = true
        val client = ApiConfig().getApiService().getSpecificStory(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _storyData.value = responseBody.story
                    }
                } else {
                    Log.d(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure : ${t.message}")
            }
        })
    }
}