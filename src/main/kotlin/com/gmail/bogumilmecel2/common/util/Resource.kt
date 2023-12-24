package com.gmail.bogumilmecel2.common.util

import com.gmail.bogumilmecel2.common.domain.model.exceptions.BaseException
import com.gmail.bogumilmecel2.common.domain.model.exceptions.UnknownException

sealed class Resource<T>(open val data: T? = null) {
    data class Success<T>(override val data: T) : Resource<T>(data)
    data class Error<T>(val exception: BaseException = UnknownException, override val data: T? = null) : Resource<T>(data)
}

fun <T> Resource.Error<*>.copyType() = Resource.Error<T>(
    exception = exception,
    data = null
)