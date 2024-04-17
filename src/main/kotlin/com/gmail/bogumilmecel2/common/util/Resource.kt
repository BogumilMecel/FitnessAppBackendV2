package com.gmail.bogumilmecel2.common.util

import com.gmail.bogumilmecel2.common.domain.model.exceptions.BaseException
import io.ktor.http.*

sealed class Resource<T>(open val data: T? = null) {
    data class Success<T>(override val data: T) : Resource<T>()
    data class Error<T>(
        val exception: BaseException? = null,
        val httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest,
        val errorMessage: ErrorMessage? = null,
    ) : Resource<T>()
}

fun <T> Resource.Error<*>.copyType() = Resource.Error<T>(
    httpStatusCode = httpStatusCode,
    errorMessage = errorMessage
)