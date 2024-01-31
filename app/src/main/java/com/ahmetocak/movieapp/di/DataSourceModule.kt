package com.ahmetocak.movieapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSource
import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.local.watch_list.WatchListLocalDataSource
import com.ahmetocak.movieapp.data.datasource.local.watch_list.WatchListLocalDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.local.watch_list.db.WatchListDao
import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.firebase.firestore.FirebaseFirestoreDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.firestore.FirebaseFirestoreDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.firebase.storage.FirebaseStorageDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.storage.FirebaseStorageDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.movie.MovieRemoteDataSource
import com.ahmetocak.movieapp.data.datasource.remote.movie.MovieRemoteDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
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
    fun provideFirebaseAuthDataSource(firebaseAuth: FirebaseAuth): FirebaseAuthDataSource {
        return FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideDataStoreDataSource(datastore: DataStore<Preferences>): DataStoreDataSource {
        return DataStoreDataSourceImpl(datastore)
    }

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(api: MovieApi): MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(api)
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
    fun provideWatchListLocalDataSource(dao: WatchListDao): WatchListLocalDataSource {
        return WatchListLocalDataSourceImpl(dao)
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