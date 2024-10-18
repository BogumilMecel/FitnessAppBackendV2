package com.gmail.bogumilmecel2.user.dates.routes

import com.gmail.bogumilmecel2.common.util.extensions.getTimezoneHeader
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.user.dates.domain.use_cases.GetAvailableDiaryDatesUseCase
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.configureGetAvailableDiaryDatesRoute(getAvailableDiaryDatesUseCase: GetAvailableDiaryDatesUseCase) {
    authenticate {
        get("/available_diary_dates/") {
            call.run {
                getTimezoneHeader()?.let { timezone ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = getAvailableDiaryDatesUseCase(userId = userId)
                        )
                    }
                }
            }
        }
    }
}