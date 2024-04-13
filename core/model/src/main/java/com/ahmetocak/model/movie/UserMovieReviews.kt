package com.ahmetocak.model.movie

import androidx.compose.runtime.Immutable

@Immutable
data class UserReviewResults(
    val id: String,
    val author: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val authorDetails: UserReviewAuthorDetails
)

@Immutable
data class UserReviewAuthorDetails(
    val rating: Int? = null,
    val avatarPath: String? = null
)