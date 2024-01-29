package com.ahmetocak.movieapp.data.datasource.remote.firebase.firestore

import com.ahmetocak.movieapp.model.firebase.firestore.WatchListMovie
import com.ahmetocak.movieapp.utils.Firestore
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class FirebaseFirestoreDataSourceImpl @Inject constructor(
    private val firestoreDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FirebaseFirestoreDataSource {

    override fun addMovieData(watchListMovie: WatchListMovie): Task<Void> {
        return firestoreDb.collection(Firestore.WATCH_LIST_COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .set(
                hashMapOf(Firestore.WATCH_LIST_ARRAY_NAME to FieldValue.arrayUnion(watchListMovie)),
                SetOptions.merge()
            )
    }

    override fun removeMovieData(watchListMovie: WatchListMovie): Task<Void> {
        return firestoreDb.collection(Firestore.WATCH_LIST_COLLECTION_KEY)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .update(mapOf(Firestore.WATCH_LIST_ARRAY_NAME to FieldValue.arrayRemove(watchListMovie)))
    }
}