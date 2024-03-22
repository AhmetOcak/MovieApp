package com.ahmetocak.network.model.movie

import com.google.gson.annotations.SerializedName

data class NetworkUserMovieReviews(
    val page: Int,
    val results: List<NetworkUserReviewResults>,

    @SerializedName("total_pages")
    val totalPages: Int,
)

data class NetworkUserReviewResults(
    val id: String,
    val author: String? = null,
    val content: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("author_details")
    val authorDetails: NetworkUserReviewAuthorDetails
)

data class NetworkUserReviewAuthorDetails(
    val rating: Int? = null,

    @SerializedName("avatar_path")
    val avatarPath: String? = null
)