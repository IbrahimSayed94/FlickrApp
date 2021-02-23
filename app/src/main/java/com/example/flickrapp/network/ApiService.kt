package com.example.flickrapp.network

import com.example.flickrapp.SearchPhoto.PhotosResponse
import com.example.flickrapp.utils.Constant
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Constant.REST_API)
    fun searchPhotos(
        @Query("page") page : Int,
        @Query("per_page") perPage : Int,
        @Query("method") method : String ?= Constant.METHOD_SEARCH_PHOTOS,
        @Query("format") format : String ?= "json",
        @Query("nojsoncallback") noJsonCallback : String ?= "50",
        @Query("text") text : String ?= "Color",
        @Query("api_key") apiKey : String ?= Constant.API_KEY,
    ) : Call<PhotosResponse>

}