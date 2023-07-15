package com.gmail.bogumilmecel2.diary_feature.routes.recipe

import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.GetRecipeUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetRecipeRoute(getRecipeUseCase: GetRecipeUseCase) {
    authenticate {
        get("/recipe/{recipeId}") {
            call.run {
                getParameter("recipeId")?.let { recipeId ->
                    handleResource(
                        resource = getRecipeUseCase(recipeId)
                    )
                }
            }
        }
    }
}