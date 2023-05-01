package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.paging.StoryRepository
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiServiceForPaging()
        return StoryRepository(database, apiService)
    }
}