package com.ahmetocak.network.model.movie_detail

import com.google.gson.annotations.SerializedName

data class NetworkMovieCredit(
    val cast: List<NetworkCast>,

    @SerializedName("crew")
    val crew: List<NetworkCrew>
)

data class NetworkCast(
    val id: Int,
    val name: String?,

    @SerializedName("character")
    val characterName: String?,

    @SerializedName("profile_path")
    val imageUrlPath: String?
)

data class NetworkCrew(
    val name: String?,
    val job: String?
)