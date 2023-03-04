package com.gmail.bogumilmecel2.diary_feature.routes.recipe

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.AddRecipeDiaryEntryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configurePostRecipeDiaryEntry(
    addRecipeDiaryEntryUseCase: AddRecipeDiaryEntryUseCase
) {
    authenticate {
        post("/recipe") {
            call.run {
                receiveOrRespond<RecipeDiaryEntryRequest>()?.let { request ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = addRecipeDiaryEntryUseCase(
                                request = request,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}