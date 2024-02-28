package com.ahmetocak.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.Locale

class LocaleManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "language_preference",
        Context.MODE_PRIVATE
    )

    private val languageKey = "language"

    /**
     * Retrieves the selected language for the app from SharedPreferences.
     *
     * @return A [String] representing the selected language code for the app.
     */
    fun getAppLanguage(): String {
        return sharedPreferences.getString(languageKey, Locale.getDefault().language)
            ?: Locale.getDefault().language
    }

    /**
     * Sets the selected language for the app and saves it in SharedPreferences.
     *
     * @param languageCode A [String] representing the language code to be set for the app.
     */
    fun setAppLanguage(languageCode: String) {
        sharedPreferences.edit {
            putString(languageKey, languageCode)
            apply()
        }
    }

    /**
     * Creates a new Context with the specified language code, updating the app's locale.
     *
     * This function takes a [languageCode] parameter representing the desired language code
     * and creates a new Context with the updated locale. It sets the default locale and updates
     * the configuration of the provided [context] to reflect the chosen language.
     *
     * @param languageCode A [String] representing the language code to set for the new locale.
     * @return A new Context with the updated locale.
     */
    fun getLocale(languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}