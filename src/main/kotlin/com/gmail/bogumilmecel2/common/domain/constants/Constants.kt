package com.gmail.bogumilmecel2.common.domain.constants

object Constants {

    const val DEFAULT_PAGE_SIZE = 20

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
        const val BARCODE_MIN_LENGTH = 1
        const val MAXIMUM_MODIFY_DATE = 14
    }

    object Weight {
        const val MINIMUM_ENTRIES_COUNT = 2
    }

    object Api {
        const val PAGE = "page"
        const val SEARCH_TEXT = "search_text"
        const val TIME_ZONE = "time_zone"
        const val CURRENCY = "currency"
        const val COUNTRY = "country"
        const val DEVICE_ID = "device_id"
        const val LATEST_TIMESTAMP = "latest_timestamp"
    }
}