package com.gmail.bogumilmecel2.common.plugins

import com.github.aymanizz.ktori18n.I18n
import io.ktor.server.application.*
import java.util.*

fun Application.configureInternationalization() {
    install(I18n) {
        supportedLocales = listOf(Locale.ENGLISH)
        defaultLocale = Locale.ENGLISH
    }
}