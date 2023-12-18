package com.gmail.bogumilmecel2.diary_feature.routes.recipe

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.recipe.RecipeDiaryEntry
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.recipe.EditRecipeDiaryEntryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureEditRecipeDiaryEntryRoute(
    editRecipeDiaryEntryUseCase: EditRecipeDiaryEntryUseCase
) {
    authenticate {
        put("/recipe") {
            call.run {
                receiveOrRespond<RecipeDiaryEntry>()?.let { recipeDiaryEntry ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = editRecipeDiaryEntryUseCase(
                                recipeDiaryEntry = recipeDiaryEntry,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}