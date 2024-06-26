package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getDateTimeParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetUserProductDiaryEntriesUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetUserProductDiaryEntriesRoute(getUserProductDiaryEntriesUseCase: GetUserProductDiaryEntriesUseCase) {
    authenticate {
        get("/product_diary") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getUserProductDiaryEntriesUseCase(
                            userId = userId,
                            latestDateTime = getDateTimeParameter()
                        )
                    )
                }
            }
        }
    }
}