package com.gmail.bogumilmecel2.common.util.extensions

fun String.isLengthInRange(maximum: Int, minimum: Int = 0): Boolean = this.length in minimum until maximum