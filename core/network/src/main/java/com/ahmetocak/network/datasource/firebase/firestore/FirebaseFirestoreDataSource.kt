package com.ahmetocak.network.datasource.firebase.firestore

import com.ahmetocak.network.model.firebase.firestore.NetworkWatchListMovie
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface FirebaseFirestoreDataSource {

    fun addMovieData(watchListMovie: NetworkWatchListMovie): Task<Void>

    fun updateMovieData(watchListMovie: List<NetworkWatchListMovie>): Task<Void>

    fun getMovieData(): Task<DocumentSnapshot>

    fun deleteMovieDocument(): Task<Void>
}