package com.gmail.bogumilmecel2.common.domain.model

import com.github.aymanizz.ktori18n.R
import io.ktor.http.*

data class CustomException(
    val message: R? = null,
    val httpStatusCode: HttpStatusCode = HttpStatusCode.Conflict
)