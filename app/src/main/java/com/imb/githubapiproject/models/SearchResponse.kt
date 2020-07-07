package com.imb.githubapiproject.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("total_count") var totalCount: Long,
    @SerializedName("incomplete_results") var incRes: Boolean,
    @SerializedName("items") var repositories: List<Repository>
)