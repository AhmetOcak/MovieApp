package com.ahmetocak.movieapp.model.movie_detail

import com.google.gson.annotations.SerializedName

data class MovieCreditDto(
    val cast: List<CastDto>,

    @SerializedName("crew")
    val crew: List<CrewDto>
)

data class CastDto(
    val id: Int,
    val name: String?,

    @SerializedName("character")
    val characterName: String?,

    @SerializedName("profile_path")
    val imageUrlPath: String?
)

data class CrewDto(
    val name: String?,
    val job: String?
)