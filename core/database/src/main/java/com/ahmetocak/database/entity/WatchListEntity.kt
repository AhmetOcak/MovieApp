package com.ahmetocak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchListEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val movieId: Int,

    @ColumnInfo("movie_name")
    val movieName: String,

    @ColumnInfo("release_year")
    val releaseYear: String,

    @ColumnInfo("vote_average")
    val voteAverage: Float,

    @ColumnInfo("vote_count")
    val voteCount: Int,

    @ColumnInfo("image_url_path")
    val imageUrlPath: String?
)
