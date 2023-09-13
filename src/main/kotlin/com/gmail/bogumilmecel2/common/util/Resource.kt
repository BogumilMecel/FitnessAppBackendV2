package com.gmail.bogumilmecel2.common.util

import com.github.aymanizz.ktori18n.R
import com.gmail.bogumilmecel2.common.domain.model.CustomException
import io.ktor.http.*

sealed class Resource<T>(open val data: T? = null, open val error: CustomException? = null) {
    data class Success<T>(override val data: T) : Resource<T>(data)
    data class Error<T>(override val error: CustomException = CustomException(), override val data: T? = null) : Resource<T>(data, error) {
        companion object {
            fun <T> create(message: R, httpStatusCode: HttpStatusCode = HttpStatusCode.BadRequest) = Error<T>(
                error = CustomException(
                    message = message,
                    httpStatusCode = httpStatusCode
                )
            )
        }
    }
}

fun <T> Resource.Error<*>.copyType() = Resource.Error<T>(
    error = CustomException(
        message = this.error.message,
        httpStatusCode = this.error.httpStatusCode
    ),
    data = null
)