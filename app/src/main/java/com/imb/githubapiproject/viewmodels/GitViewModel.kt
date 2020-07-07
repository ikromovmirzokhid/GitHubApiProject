package com.imb.githubapiproject.viewmodels

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imb.githubapiproject.api.GitApiService
import com.imb.githubapiproject.di.component.DaggerGitComponent
import com.imb.githubapiproject.di.module.GitModule
import com.imb.githubapiproject.models.Repository
import com.imb.githubapiproject.models.SearchResponse
import com.imb.githubapiproject.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GitViewModel : ViewModel() {
    @Inject
    lateinit var api: GitApiService
    private var userData: MutableLiveData<User> = MutableLiveData<User>()
    private var userRepos: MutableLiveData<List<Repository>> = MutableLiveData()
    private var searchResult: MutableLiveData<SearchResponse> = MutableLiveData()

    init {
        val gitComponent = DaggerGitComponent.builder().gitModule(GitModule()).build()
        gitComponent.inject(this)
    }

    fun login(username: String, password: String): LiveData<User>? {
        val base = "$username:$password"
        val authHeader = "Basic " + Base64.encodeToString(base.toByteArray(), Base64.NO_WRAP)
        api.login(authHeader).enqueue(object : retrofit2.Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                userData.value = null
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                userData.value = response.body()
            }

        })

        return userData
    }

    fun getUserRepos(userName: String): LiveData<List<Repository>> {
        api.getOwnerRepos(userName).enqueue(object : Callback<List<Repository>> {
            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                userRepos.value = null
            }

            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                if (response.isSuccessful)
                    userRepos.value = response.body()
                else
                    userRepos.value = null
            }

        })

        return userRepos
    }

    fun searchRepositoryByName(reposName: String, page: Int): LiveData<SearchResponse> {
        api.searchRepositories(reposName, page).enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                searchResult.value = null
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful)
                    searchResult.value = response.body()
                else
                    searchResult.value = null
            }

        })
        return searchResult
    }
}