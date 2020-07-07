package com.imb.githubapiproject.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") var username: String,
    @SerializedName("id") var id: String,
    @SerializedName("node_id") var nodeId: String,
    @SerializedName("avatar_url") var avatarUrl: String,
    @SerializedName("url") var url: String
)