package com.imb.githubapiproject.api

import com.imb.githubapiproject.models.Repository
import com.imb.githubapiproject.models.SearchResponse
import com.imb.githubapiproject.models.User
import retrofit2.Call
import retrofit2.http.*

interface GitApiService {

    @GET("user")
    fun login(@Header("Authorization") authHeader: String): Call<User>

    @GET("users/{user}/repos")
    fun getOwnerRepos(@Path("user") userName: String): Call<List<Repository>>

    @GET("search/repositories")
    fun searchRepositories(
        @Query("q") reposName: String,
        @Query("page") page: Int
    ): Call<SearchResponse>
}