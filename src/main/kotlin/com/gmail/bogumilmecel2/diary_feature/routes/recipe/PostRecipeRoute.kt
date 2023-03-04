package com.gmail.bogumilmecel2.diary_feature.routes.recipe

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.NewRecipeRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.InsertRecipeUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configurePostRecipeRoute(
    insertRecipeUseCase: InsertRecipeUseCase
) {
    authenticate {
        post("") {
            call.run {
                receiveOrRespond<NewRecipeRequest>()?.let { newRecipeRequest ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = insertRecipeUseCase(
                                newRecipeRequest = newRecipeRequest,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}