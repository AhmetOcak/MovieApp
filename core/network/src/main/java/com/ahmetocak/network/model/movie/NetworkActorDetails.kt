package com.ahmetocak.network.model.movie

import com.google.gson.annotations.SerializedName

data class NetworkActorDetails(
    val id: Int,
    val biography: String?,
    val birthday: String?,
    val homepage: String?,
    val name: String?,

    @SerializedName("deathday")
    val deathDay: String?,

    @SerializedName("place_of_birth")
    val placeOfBirth: String?,

    @SerializedName("profile_path")
    val profileImagePath: String?
)