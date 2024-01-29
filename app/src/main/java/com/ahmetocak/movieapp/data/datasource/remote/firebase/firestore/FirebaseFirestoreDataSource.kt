package com.ahmetocak.movieapp.data.datasource.remote.firebase.firestore

import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.google.android.gms.tasks.Task

interface FirebaseFirestoreDataSource {

    fun addMovieData(watchListMovie: WatchListMovie): Task<Void>
}