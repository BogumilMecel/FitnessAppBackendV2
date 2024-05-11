package com.gmail.bogumilmecel2.common.domain.util

import com.gmail.bogumilmecel2.common.util.Resource
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import org.litote.kmongo.coroutine.CoroutineFindPublisher

open class BaseRepository() {

    inline fun <R> handleRequest(block: () -> R): Resource<R> {
        return try {
            Resource.Success(block())
        } catch (e: Exception) {
            handleExceptionWithResource(exception = e)
        }
    }

    fun <T> handleExceptionWithResource(exception: Exception): Resource<T> {
        exception.printStackTrace()
        return Resource.Error()
    }

    protected fun InsertOneResult.wasAcknowledgedOrThrow() = if (this.wasAcknowledged()) true else throw Exception()
    protected fun UpdateResult.wasAcknowledgedOrThrow() = if (this.wasAcknowledged()) true else throw Exception()
    protected inline fun <T: Any, R: Any> CoroutineFindPublisher<R>.optionalFilter(
        optionalParameter: T?,
        block: CoroutineFindPublisher<R>.(T) -> CoroutineFindPublisher<R>
    ) : CoroutineFindPublisher<R> {
        return optionalParameter?.let {
            block(it)
        } ?: this
    }
}