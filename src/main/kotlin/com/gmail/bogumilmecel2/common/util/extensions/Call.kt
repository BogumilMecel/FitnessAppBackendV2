package com.gmail.bogumilmecel2.common.util.extensions

import com.github.aymanizz.ktori18n.t
import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.domain.constants.Constants.Api.DATE
import com.gmail.bogumilmecel2.common.domain.constants.Constants.Api.LATEST_DATE_TIME
import com.gmail.bogumilmecel2.common.domain.model.Country
import com.gmail.bogumilmecel2.common.domain.model.Currency
import com.gmail.bogumilmecel2.common.domain.model.exceptions.BaseException
import com.gmail.bogumilmecel2.common.util.Resource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend inline fun <reified T> ApplicationCall.handleResource(resource: Resource<T>) {
    when (resource) {
        is Resource.Success -> {
            this.respondNullable(
                status = HttpStatusCode.OK,
                message = resource.data
            )
        }

        is Resource.Error -> {
            println("Exception in ${request.path()} " + resource.httpStatusCode)
            this.respondNullable(
                status = resource.httpStatusCode,
                message = resource.exception?.resource?.let { t(it) } ?: resource.errorMessage
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
    return runCatching { this.receiveNullable<T>() }.getOrNull() ?: run {
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

fun ApplicationCall.getNullableParameter(name: String): String? {
    return this.parameters[name]
}

fun ApplicationCall.getDateTimeParameter(name: String? = null): LocalDateTime? {
    return this.parameters[name ?: LATEST_DATE_TIME]?.toLocalDateTime()
}

suspend fun ApplicationCall.getDateParameter(name: String? = null): LocalDate? {
    return this.parameters[name ?: DATE]?.toLocalDate() ?: run {
        respondBadRequest(this)
        null
    }
}

suspend inline fun ApplicationCall.getCountryHeader(): Country? {
    return this.request.headers[Constants.Api.COUNTRY]?.let {
        Country.getCountryFromString(it)
    } ?: kotlin.run {
        respondBadRequest(this)
        null
    }
}


suspend inline fun ApplicationCall.getCurrencyHeader(): Currency? {
    return try {
        this.request.headers[Constants.Api.CURRENCY]?.let {
            Currency.valueOf(it)
        }
    } catch (e: Exception) {
        null
    } ?: kotlin.run {
        respondBadRequest(this)
        null
    }
}

suspend inline fun ApplicationCall.getTimezoneHeader(): TimeZone? {
    return try {
        this.request.headers[Constants.Api.TIME_ZONE]?.let {
            TimeZone.of(zoneId = it)
        }
    } catch (e: Exception) {
        null
    } ?: kotlin.run {
        respondBadRequest(this)
        null
    }
}

suspend inline fun ApplicationCall.getDeviceIdHeader(): String? {
    return try {
        this.request.headers[Constants.Api.DEVICE_ID]
    } catch (e: Exception) {
        null
    } ?: kotlin.run {
        respondBadRequest(this)
        null
    }
}

suspend fun respondBadRequest(call: ApplicationCall) {
    call.respond(HttpStatusCode.BadRequest)
}

suspend inline fun <reified T> ApplicationCall.handleRequest(block: () -> T) = runCatching(block)
    .onSuccess {
        this.respondNullable(
            status = HttpStatusCode.OK,
            message = it
        )
    }
    .onFailure {
        this.respondNullable(
            status = if (it is BaseException) {
                it.httpStatusCode
            } else {
                HttpStatusCode.InternalServerError
            },
            message = if (it is BaseException) {
                t(it.resource)
            } else {
                "Server error"
            }
        )
    }