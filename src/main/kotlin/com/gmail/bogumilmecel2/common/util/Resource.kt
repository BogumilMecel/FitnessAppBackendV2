package com.gmail.bogumilmecel2.common.util

import com.gmail.bogumilmecel2.common.domain.model.CustomException

sealed class Resource<T>(open val data: T? = null, open val error: CustomException? = null) {
    data class Success<T>(override val data: T) : Resource<T>(data)
    data class Error<T>(override val error: CustomException = CustomException(), override val data: T? = null) : Resource<T>(data, error)
}