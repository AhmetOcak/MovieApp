package com.ahmetocak.data.di

import com.ahmetocak.data.repository.datastore.DataStoreRepository
import com.ahmetocak.data.repository.datastore.DataStoreRepositoryImpl
import com.ahmetocak.data.repository.firebase.FirebaseRepository
import com.ahmetocak.data.repository.firebase.FirebaseRepositoryImpl
import com.ahmetocak.data.repository.movie.MovieRepository
import com.ahmetocak.data.repository.movie.MovieRepositoryImpl
import com.ahmetocak.database.datasource.WatchListLocalDataSource
import com.ahmetocak.datastore.datasource.MovieAppPreferenceDataSource
import com.ahmetocak.network.api.MovieApi
import com.ahmetocak.network.datasource.firebase.firestore.FirebaseFirestoreDataSource
import com.ahmetocak.network.datasource.firebase.storage.FirebaseStorageDataSource
import com.ahmetocak.network.datasource.movie.MovieRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideFirebaseRepository(
        firebaseFirestoreDataSource: FirebaseFirestoreDataSource,
        firebaseStorageDataSource: FirebaseStorageDataSource
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(
            firebaseFirestoreDataSource,
            firebaseStorageDataSource
        )
    }

    @Singleton
    @Provides
    fun providePreferenceDataStoreRepository(
        preferenceDataSource: MovieAppPreferenceDataSource
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(preferenceDataSource)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource,
        api: MovieApi,
        watchListLocalDataSource: WatchListLocalDataSource
    ): MovieRepository {
        return MovieRepositoryImpl(movieRemoteDataSource, api, watchListLocalDataSource)
    }
}