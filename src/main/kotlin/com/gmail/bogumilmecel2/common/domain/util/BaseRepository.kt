package com.gmail.bogumilmecel2.common.domain.util

import com.gmail.bogumilmecel2.common.domain.model.CustomException
import com.gmail.bogumilmecel2.common.util.Resource

open class BaseRepository() {

    suspend inline fun <R> handleRequest(block: () -> R): Resource<R> {
        return try {
            Resource.Success(block())
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    fun <T> handleExceptionWithResource(exception: Exception, data: T? = null): Resource<T> {
        exception.printStackTrace()
        return Resource.Error(
            error = CustomException(exception = exception),
            data = data
        )
    }
}