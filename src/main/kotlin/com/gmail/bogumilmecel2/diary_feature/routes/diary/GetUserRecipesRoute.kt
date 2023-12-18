package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.domain.constants.Constants
import com.gmail.bogumilmecel2.common.util.extensions.getNullableParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.GetUserRecipesUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetUserRecipesRoute(getUserRecipesUseCase: GetUserRecipesUseCase) {
    authenticate {
        get("/recipe") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getUserRecipesUseCase(
                            userId = userId,
                            latestTimestamp = getNullableParameter(name = Constants.Api.LATEST_TIMESTAMP)
                        )
                    )
                }
            }
        }
    }
}