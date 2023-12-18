package com.gmail.bogumilmecel2.common.util.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun String.isLengthInRange(maximum: Int, minimum: Int = 0): Boolean = length in minimum..maximum

fun String.toLocalDateTime() = try {
    LocalDateTime.parse(this)
} catch (e: Exception) {
    null
}

fun String.toLocalDate() = try {
    LocalDate.parse(this)
} catch (e: Exception) {
    null
}