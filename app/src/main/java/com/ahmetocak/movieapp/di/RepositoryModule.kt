package com.ahmetocak.movieapp.di

import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSource
import com.ahmetocak.movieapp.data.datasource.remote.firebase.auth.FirebaseAuthDataSource
import com.ahmetocak.movieapp.data.repository.datastore.DataStoreRepository
import com.ahmetocak.movieapp.data.repository.datastore.DataStoreRepositoryImpl
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepositoryImpl
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
}