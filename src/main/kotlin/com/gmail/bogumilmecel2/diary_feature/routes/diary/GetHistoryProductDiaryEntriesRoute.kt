package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getDateTimeParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetHistoryProductDiaryEntriesUseCase
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetHistoryProductDiaryEntriesRoute(getHistoryProductDiaryEntriesUseCase: GetHistoryProductDiaryEntriesUseCase) {
    authenticate {
        get("/history_product_diary") {
            call.handleRequest(
                block = {
                    getHistoryProductDiaryEntriesUseCase(
                        userId = call.getUserId(),
                        latestDateTime = call.getDateTimeParameter()
                    )
                }
            )
        }
    }
} 