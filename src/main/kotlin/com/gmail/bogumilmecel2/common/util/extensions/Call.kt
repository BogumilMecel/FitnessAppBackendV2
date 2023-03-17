package com.gmail.bogumilmecel2.common.util.extensions

import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.util.Resource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend inline fun <reified T> ApplicationCall.handleResource(resource: Resource<T>) {
    when (resource) {
        is Resource.Success -> {
            resource.data?.let {
                this.respond(
                    status = HttpStatusCode.OK,
                    message = resource.data
                )
            } ?: kotlin.run {
                this.respond(
                    HttpStatusCode.Conflict,
                )
            }
        }

        is Resource.Error -> {
            println("Exception in ${this.request.path()} " + resource.error)
            this.respond(
                status = resource.error.httpStatusCode,
                message = resource.error.exception.message ?: ""
            )
        }
    }
}

suspend fun ApplicationCall.getUserId(): String? {
    this.principal<JWTPrincipal>()?.getClaim("userId", String::class)?.let { userId ->
        return suspendCoroutine { continuation ->
            continuation.resume(userId)
        }
    } ?: kotlin.run {
        this@getUserId.respond(HttpStatusCode.Unauthorized)
        return null
    }
}

suspend inline fun <reified T : Any> ApplicationCall.receiveOrRespond(): T? {
    return this.receiveOrNull() ?: kotlin.run {
        respondBadRequest(this)
        null
    }
}

suspend inline fun ApplicationCall.getParameter(name: String): String? {
    return this.parameters[name] ?: kotlin.run {
        respondBadRequest(this)
        null
    }
}

suspend inline fun ApplicationCall.getCountryParameter(): Country? {
    return this.request.headers["country"]?.let {
        Country.getCountryFromString(it)
    } ?: kotlin.run {
        respondBadRequest(this)
        null
    }
}

suspend fun respondBadRequest(call: ApplicationCall) {
    call.respond(HttpStatusCode.BadRequest)
}