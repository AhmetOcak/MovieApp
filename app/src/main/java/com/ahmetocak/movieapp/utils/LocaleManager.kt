package com.ahmetocak.movieapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.datastore: DataStore<Preferences>
    by preferencesDataStore(DataStoreConstants.APP_LANGUAGE_FILE_NAME)

object LanguageCodes {
    const val TR = "tr"
    const val EN = "en"
}

class LocaleManager(private val context: Context) {
    companion object {
        private val LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    suspend fun getAppLanguage(): String {
        return context.datastore.data.map { preference ->
            preference[LANGUAGE_CODE] ?: Locale.getDefault().language
        }.first()
    }

    suspend fun setAppLanguage(languageCode: String) {
        context.datastore.edit { settings ->
            settings[LANGUAGE_CODE] = languageCode
        }
    }

    fun getLocale(languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}