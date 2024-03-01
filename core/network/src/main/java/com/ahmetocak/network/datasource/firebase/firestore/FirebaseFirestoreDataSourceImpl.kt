package com.ahmetocak.network.datasource.firebase.firestore

import com.ahmetocak.network.model.firebase.firestore.NetworkWatchListMovie
import com.ahmetocak.network.utils.FirebaseConstants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class FirebaseFirestoreDataSourceImpl @Inject constructor(
    private val firestoreDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : FirebaseFirestoreDataSource {

    override fun addMovieData(watchListMovie: NetworkWatchListMovie): Task<Void> {
        return firestoreDb.collection(FirebaseConstants.Firestore.WATCH_LIST_COLLECTION_NAME)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .set(
                hashMapOf(FirebaseConstants.Firestore.WATCH_LIST_ARRAY_NAME to FieldValue.arrayUnion(watchListMovie)),
                SetOptions.merge()
            )
    }

    override fun updateMovieData(watchListMovie: List<NetworkWatchListMovie>): Task<Void> {
        return firestoreDb.collection(FirebaseConstants.Firestore.WATCH_LIST_COLLECTION_NAME)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .update(mapOf(FirebaseConstants.Firestore.WATCH_LIST_ARRAY_NAME to watchListMovie))
    }

    override fun getMovieData(): Task<DocumentSnapshot> {
        return firestoreDb.collection(FirebaseConstants.Firestore.WATCH_LIST_COLLECTION_NAME)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .get()
    }

    override fun deleteMovieDocument(): Task<Void> {
        return firestoreDb.collection(FirebaseConstants.Firestore.WATCH_LIST_COLLECTION_NAME)
            .document(firebaseAuth.currentUser?.uid ?: "")
            .delete()
    }
}