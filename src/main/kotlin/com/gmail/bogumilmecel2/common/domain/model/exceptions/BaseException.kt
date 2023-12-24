package com.gmail.bogumilmecel2.common.domain.model.exceptions

import com.github.aymanizz.ktori18n.R
import io.ktor.http.*

abstract class BaseException(
    open val resource: R,
    val httpStatusCode: HttpStatusCode
) : Exception()

abstract class BadRequestException(override val resource: R) : BaseException(
    resource = resource,
    httpStatusCode = HttpStatusCode.BadRequest
)

abstract class NotFoundException(override val resource: R) : BaseException(
    resource = resource,
    httpStatusCode = HttpStatusCode.NotFound
)

data object ForbiddenException : BaseException(
    resource = R("access_forbidden"),
    httpStatusCode = HttpStatusCode.Forbidden
)

data object UnknownException : BaseException(
    resource = R("unknown_error"),
    httpStatusCode = HttpStatusCode.Conflict
)