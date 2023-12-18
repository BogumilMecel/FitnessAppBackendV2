package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetUserRecipeDiaryEntriesUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetUserRecipeDiaryEntriesRoute(getUserRecipeDiaryEntriesUseCase: GetUserRecipeDiaryEntriesUseCase) {
    authenticate {
        get("/recipe_diary") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getUserRecipeDiaryEntriesUseCase(
                            userId = userId,
                        )
                    )
                }
            }
        }
    }
}