package com.ahmetocak.movieapp.model.movie_detail

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class MovieCreditDto(
    val cast: List<CastDto>,

    @SerializedName("crew")
    val crew: List<CrewDto>
)

@Immutable
data class CastDto(
    val id: Int,
    val name: String?,

    @SerializedName("character")
    val characterName: String?,

    @SerializedName("profile_path")
    val imageUrlPath: String?
)

@Immutable
data class CrewDto(
    val name: String?,
    val job: String?
)