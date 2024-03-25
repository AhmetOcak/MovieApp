package com.ahmetocak.domain

import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.R as common
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject

class GetGeminiResponseUseCase @Inject constructor() {

    suspend operator fun invoke(prompt: String): Response<String?> {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.GEMINI_API_KEY
        )

        return try {
            Response.Success(data = generativeModel.generateContent(prompt).text)
        } catch (e: Exception) {
            Response.Error(errorMessage = e.message?.let { message ->
                UiText.DynamicString(message)
            } ?: UiText.StringResource(common.string.unknown_error))
        }
    }
}