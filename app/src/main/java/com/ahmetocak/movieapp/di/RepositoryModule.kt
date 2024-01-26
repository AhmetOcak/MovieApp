package com.ahmetocak.movieapp.di

import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.movieapp.data.datasource.remote.movie.MovieRemoteDataSource
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
import com.ahmetocak.movieapp.data.repository.datastore.DataStoreRepository
import com.ahmetocak.movieapp.data.repository.datastore.DataStoreRepositoryImpl
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepositoryImpl
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.data.repository.movie.MovieRepositoryImpl
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
        firebaseAuthDataSource: FirebaseAuthDataSource
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseAuthDataSource)
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        dataStoreDataSource: DataStoreDataSource
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStoreDataSource)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource,
        api: MovieApi
    ): MovieRepository {
        return MovieRepositoryImpl(movieRemoteDataSource, api)
    }
}