package com.example.demomeow.common.error

import com.example.demomeow.common.DebugLog
import com.google.gson.GsonBuilder
import retrofit2.HttpException
import java.io.IOException

fun handlerException(throwable: Throwable): ApiException {
    return when (throwable) {
        is HttpException -> mapHttpThrowable(throwable) ?: ApiException.unexpectedError(throwable)
        is IOException -> ApiException.networkError(throwable)
        else -> ApiException.unexpectedError(throwable)
    }
}

private fun mapHttpThrowable(throwable: Throwable): ApiException? {
    if (throwable !is HttpException) return null
    val response = throwable.response()
    val errorResponse = response?.errorBody()?.let { errorBody ->
        val errorResponse = errorBody.string()
        deserializeServerError(errorResponse)
    }
    return when {
        errorResponse != null -> ApiException.serverError(errorResponse)
        response != null -> ApiException.httpError(response)
        else -> ApiException.unexpectedError(throwable)
    }
}

private fun deserializeServerError(errorString: String): BaseErrorResponse? =
    try {
        GsonBuilder().create().fromJson(errorString, BaseErrorResponse::class.java)
    } catch (e: Exception) {
        DebugLog().e(e.stackTraceToString())
        null
    }
