package com.gmail.bogumilmecel2.common.domain.model

import io.ktor.http.*

data class CustomException(
    val exception: Exception = Exception(),
    val httpStatusCode: HttpStatusCode = HttpStatusCode.Conflict
)
