package com.ahmetocak.movieapp.data.datasource.remote.firebase.firestore

import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface FirebaseFirestoreDataSource {

    fun addMovieData(watchListMovie: WatchListMovie): Task<Void>

    fun removeMovieData(watchListMovie: WatchListMovie): Task<Void>

    fun getMovieData(): Task<DocumentSnapshot>

    fun deleteMovieDocument(): Task<Void>
}