package com.example.storyapp

import com.example.storyapp.data.retrofit.response.ListStoryItem

object DummyData {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-$i.jpg",
                "createdAt + $i",
                "author $i",
                "description $i",
                null,
                "$i",
                null

            )
            items.add(story)
        }
        return items
    }
}