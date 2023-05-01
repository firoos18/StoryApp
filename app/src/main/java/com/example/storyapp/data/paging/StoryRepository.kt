package com.example.storyapp.data.paging

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.data.retrofit.response.ListStoryItem
import com.example.storyapp.database.StoryDatabase

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory() : LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}