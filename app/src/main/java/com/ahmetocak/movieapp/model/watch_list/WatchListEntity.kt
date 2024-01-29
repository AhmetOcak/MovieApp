package com.ahmetocak.movieapp.model.watch_list

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity
data class WatchListEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val movieId: Int,

    @ColumnInfo("movie_name")
    val movieName: String,

    @ColumnInfo("release_year")
    val releaseYear: String,

    @ColumnInfo("genres")
    val genres: String,

    @ColumnInfo("vote_average")
    val voteAverage: Float,

    @ColumnInfo("vote_count")
    val voteCount: Int,

    @ColumnInfo("image_url_path")
    val imageUrlPath: String
)
