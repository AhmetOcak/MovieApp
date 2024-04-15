package com.ahmetocak.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.ahmetocak.datastore.DataStoreConstants
import com.ahmetocak.datastore.datasource.MovieAppPreferenceDataSourceImpl.PreferencesKeys.APP_THEME
import com.ahmetocak.datastore.datasource.MovieAppPreferenceDataSourceImpl.PreferencesKeys.DYNAMIC_COLOR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieAppPreferenceDataSourceImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
): MovieAppPreferenceDataSource {

    private object PreferencesKeys {
        val APP_THEME = booleanPreferencesKey(DataStoreConstants.APP_THEME_KEY)
        val DYNAMIC_COLOR = booleanPreferencesKey(DataStoreConstants.DYNAMIC_COLOR_KEY)
    }

    override suspend fun observeAppTheme(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[APP_THEME] ?: true
        }
    }

    override suspend fun updateAppTheme(darkMode: Boolean) {
        datastore.edit { preferences ->
            preferences[APP_THEME] = darkMode
        }
    }

    override suspend fun observeDynamicColor(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[DYNAMIC_COLOR] ?: false
        }
    }

    override suspend fun updateDynamicColor(dynamicColor: Boolean) {
        datastore.edit { preferences ->
            preferences[DYNAMIC_COLOR] = dynamicColor
        }
    }
}