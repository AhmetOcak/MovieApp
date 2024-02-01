package com.ahmetocak.movieapp.data.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSourceImpl.PreferencesKeys.APP_THEME
import com.ahmetocak.movieapp.data.datasource.local.datastore.DataStoreDataSourceImpl.PreferencesKeys.DYNAMIC_COLOR
import com.ahmetocak.movieapp.utils.DataStoreConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreDataSourceImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
): DataStoreDataSource {

    private object PreferencesKeys {
        val APP_THEME = booleanPreferencesKey(DataStoreConstants.APP_THEME_KEY)
        val DYNAMIC_COLOR = booleanPreferencesKey(DataStoreConstants.DYNAMIC_COLOR)
    }

    override suspend fun getAppTheme(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[APP_THEME] ?: true
        }
    }

    override suspend fun updateAppTheme(darkMode: Boolean) {
        datastore.edit { settings ->
            settings[APP_THEME] = darkMode
        }
    }

    override suspend fun getDynamicColor(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[DYNAMIC_COLOR] ?: false
        }
    }

    override suspend fun updateDynamicColor(dynamicColor: Boolean) {
        datastore.edit { settings ->
            settings[DYNAMIC_COLOR] = dynamicColor
        }
    }
}