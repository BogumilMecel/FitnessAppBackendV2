package com.gmail.bogumilmecel2.common.domain.util

import com.gmail.bogumilmecel2.common.domain.model.CustomException
import com.gmail.bogumilmecel2.common.util.Resource
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult

open class BaseRepository() {

    inline fun <R> handleRequest(block: () -> R): Resource<R> {
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

    protected fun InsertOneResult.wasAcknowledgedOrThrow() = if (this.wasAcknowledged()) true else throw Exception()
    protected fun UpdateResult.wasAcknowledgedOrThrow() = if (this.wasAcknowledged()) true else throw Exception()
}