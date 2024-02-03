package com.ahmetocak.movieapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.ahmetocak.movieapp.R
import java.util.Locale

object LanguageCodes {
    const val TR = "tr"
    const val EN = "en"
}

class LocaleManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    private val languageKey = "language"

    fun getAppLanguage(): String {
        return sharedPreferences.getString(languageKey, Locale.getDefault().language)
            ?: Locale.getDefault().language
    }

    fun setAppLanguage(languageCode: String) {
        sharedPreferences.edit {
            putString(languageKey, languageCode)
            apply()
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