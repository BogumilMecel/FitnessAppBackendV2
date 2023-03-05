package com.gmail.bogumilmecel2.diary_feature.routes.recipe

import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.SearchForRecipes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureSearchForRecipesRoute(
    searchForRecipes: SearchForRecipes
) {
    authenticate {
        get("/{searchText}") {
            call.run {
                call.parameters["searchText"]?.let { searchText ->
                    handleResource(
                        resource = searchForRecipes(searchText)
                    )
                }
            }
        }
    }
}