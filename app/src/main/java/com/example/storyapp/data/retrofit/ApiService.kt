package com.example.storyapp.data.retrofit

import com.example.storyapp.data.retrofit.response.*
import com.example.storyapp.models.LoginModels
import com.example.storyapp.models.RegisterModels
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("v1/register")
    fun register(
        @Body userData : RegisterModels
    ) : Call<RegisterResponse>

    @POST("v1/login")
    fun login(
        @Body userData : LoginModels
    ) : Call<LoginResponse>

    @GET("v1/stories?")
    fun getAllStories(
//        @Header("Authorization") token : String,
        @Query("size") size : Int
    ) : Call<StoriesResponse>

    @GET("v1/stories?")
    fun getAllStoriesWithLocation(
//        @Header("Authorization") token : String,
        @Query("location") location : Int,
    ) : Call<StoriesResponse>

    @GET("v1/stories/{id}")
    fun getSpecificStory(
//        @Header("Authorization") token : String,
        @Path("id") id : String,
    ) : Call<DetailStoryResponse>

    @Multipart
    @POST("v1/stories")
    fun postStory(
//        @Header("Authorization") token : String,
        @Part file : MultipartBody.Part,
        @Part("description") description : RequestBody,
    ) : Call<PostStoryResponse>

    @GET("v1/stories?")
    suspend fun getStories(
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : StoriesResponse
}