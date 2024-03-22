package com.ahmetocak.model.movie

data class UserReviewResults(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val authorDetails: UserReviewAuthorDetails
)

data class UserReviewAuthorDetails(
    val rating: Int? = null,
    val avatarPath: String? = null
)