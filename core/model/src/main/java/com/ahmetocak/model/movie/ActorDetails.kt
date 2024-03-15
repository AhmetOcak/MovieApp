package com.ahmetocak.model.movie

import androidx.compose.runtime.Immutable

@Immutable
data class ActorDetails(
    val id: Int,
    val biography: String,
    val birthday: String,
    val homepage: String?,
    val name: String,
    val deathDay: String?,
    val placeOfBirth: String,
    val profileImagePath: String
)
