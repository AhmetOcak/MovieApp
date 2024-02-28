package com.ahmetocak.network.di

import com.ahmetocak.network.api.MovieApi
import com.ahmetocak.network.datasource.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.network.datasource.firebase.auth.FirebaseAuthDataSourceImpl
import com.ahmetocak.network.datasource.firebase.firestore.FirebaseFirestoreDataSource
import com.ahmetocak.network.datasource.firebase.firestore.FirebaseFirestoreDataSourceImpl
import com.ahmetocak.network.datasource.firebase.storage.FirebaseStorageDataSource
import com.ahmetocak.network.datasource.firebase.storage.FirebaseStorageDataSourceImpl
import com.ahmetocak.network.datasource.movie.MovieRemoteDataSource
import com.ahmetocak.network.datasource.movie.MovieRemoteDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(api: MovieApi): MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(api)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuthDataSource(firebaseAuth: FirebaseAuth): FirebaseAuthDataSource {
        return FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestoreDataSource(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FirebaseFirestoreDataSource {
        return FirebaseFirestoreDataSourceImpl(firestore, auth)
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageDataSource(
        storage: FirebaseStorage,
        auth: FirebaseAuth
    ): FirebaseStorageDataSource {
        return FirebaseStorageDataSourceImpl(storage, auth)
    }
}