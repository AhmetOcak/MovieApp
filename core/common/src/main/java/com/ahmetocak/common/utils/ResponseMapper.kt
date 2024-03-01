package com.ahmetocak.common.utils

import com.ahmetocak.common.helpers.Response

fun <I: Any, O : Any> Response<I>.mapResponse(mapper: (I) -> O): Response<O> {
    return when (this) {
        is Response.Success -> {
            Response.Success(mapper.invoke(this.data))
        }
        is Response.Error -> {
            Response.Error(errorMessage)
        }
    }
}