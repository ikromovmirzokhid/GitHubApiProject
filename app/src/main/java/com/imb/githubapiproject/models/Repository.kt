package com.imb.githubapiproject.models

import com.google.gson.annotations.SerializedName

class Repository {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("full_name")
    var fullName: String? = null

    @SerializedName("html_url")
    var url: String? = null

    @SerializedName("owner")
    var user: User? = null

    @SerializedName("created_at")
    var createdTime: String? = null

    @SerializedName("language")
    var language: String? = null

    @SerializedName("forks_count")
    var forksCount: Int? = null

    @SerializedName("stargazers_count")
    var starCounts: Int? = null
}