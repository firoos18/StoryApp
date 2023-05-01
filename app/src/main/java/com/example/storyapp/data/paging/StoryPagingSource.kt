package com.example.storyapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.retrofit.ApiService
import com.example.storyapp.data.retrofit.response.ListStoryItem
import com.example.storyapp.data.retrofit.response.StoriesResponse
import com.example.storyapp.ui.home.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseBody = apiService.getStories(position, params.loadSize)
            var responseData = mutableListOf<ListStoryItem>()
            responseBody.listStory.forEach { storiesItem ->
                MainViewModel._isLoading.value = false
                responseData.add(storiesItem)
            }

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception : Exception) {
            return LoadResult.Error(exception)
        }
    }
}