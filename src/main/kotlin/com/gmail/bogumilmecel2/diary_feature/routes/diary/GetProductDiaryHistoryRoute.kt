package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getNullableParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetProductDiaryHistoryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetProductDiaryHistoryRoute(getProductDiaryHistoryUseCase: GetProductDiaryHistoryUseCase) {
    authenticate {
        get("/history/product") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getProductDiaryHistoryUseCase(
                            userId = userId,
                            latestEntryTimestamp = getNullableParameter("latestEntryTimestamp")
                        )
                    )
                }
            }
        }
    }
}