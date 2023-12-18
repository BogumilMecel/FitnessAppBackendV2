package com.gmail.bogumilmecel2.common.util

object DateUtils {
    fun String.isValidDate(): Boolean = this.matches(regex = ("""^\d{4}-\d{2}-\d{2}$""").toRegex())
}