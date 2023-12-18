package com.gmail.bogumilmecel2.common.domain.constants

object ValidationConstants {

    object Authentication {
        const val USERNAME_MAX_LENGTH = 32
        const val USERNAME_MIN_LENGTH = 4

        const val PASSWORD_MAX_LENGTH = 64
        const val PASSWORD_MIN_LENGTH = 6
    }

    object Diary {
        const val DIARY_NAME_MAX_LENGTH = 48
        const val DIARY_NAME_MIN_LENGTH = 4

        const val BARCODE_MAX_LENGTH = 128
    }

    object Weight {
        const val MINIMUM_ENTRIES_COUNT = 2
    }
}