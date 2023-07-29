package com.gmail.bogumilmecel2.user.user_data.routes

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetUserDiaryItemsUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetUserDiaryItemsRoute(getUserDiaryItemsUseCase: GetUserDiaryItemsUseCase) {
    authenticate {
        get("/diaryItems") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getUserDiaryItemsUseCase(
                            userId = userId
                        )
                    )
                }
            }
        }
    }
}