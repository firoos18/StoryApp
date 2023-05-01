package com.example.storyapp.ui.home

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.paging.StoryRepository
import com.example.storyapp.data.retrofit.response.ListStoryItem
import com.example.storyapp.di.Injection

class MainViewModel(storyRepository: StoryRepository) : ViewModel() {
    companion object {
        var _isLoading = MutableLiveData<Boolean>()
    }

    val isLoading : LiveData<Boolean> = _isLoading

    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story : LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}