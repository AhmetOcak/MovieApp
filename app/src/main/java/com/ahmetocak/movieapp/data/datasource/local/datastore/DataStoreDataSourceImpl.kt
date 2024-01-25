package com.ahmetocak.movieapp.data.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSourceImpl.PreferencesKeys.APP_THEME
import com.ahmetocak.movieapp.utils.DataStoreConstants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreDataSourceImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
): DataStoreDataSource {

    private object PreferencesKeys {
        val APP_THEME = booleanPreferencesKey(DataStoreConstants.APP_THEME_KEY)
    }

    override suspend fun getRememberMe(): Boolean {
        return datastore.data.map { preferences ->
            preferences[APP_THEME] ?: false
        }.first()
    }

    override suspend fun updateRememberMe(value: Boolean) {
        datastore.edit { settings ->
            settings[APP_THEME] = value
        }
    }
}