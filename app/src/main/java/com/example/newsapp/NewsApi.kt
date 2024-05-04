package com.example.newsapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.Key

interface NewsApi {

    @GET("/v2/everything")
    suspend fun getNewsItems(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ):Response<NewsApiResponse>
}

