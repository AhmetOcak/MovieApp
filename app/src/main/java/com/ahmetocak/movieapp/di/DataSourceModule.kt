package com.ahmetocak.movieapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSource
import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.movie.MovieRemoteDataSource
import com.ahmetocak.movieapp.data.datasource.remote.movie.MovieRemoteDataSourceImpl
import com.ahmetocak.movieapp.data.datasource.remote.movie.api.MovieApi
import com.google.firebase.auth.FirebaseAuth
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
}