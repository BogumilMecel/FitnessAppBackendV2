package com.gmail.bogumilmecel2.user.user_data.routes

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.nutrition_values.NutritionValues
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.SaveNutritionValuesUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureSaveUserNutritionValuesRoute(saveNutritionValuesUseCase: SaveNutritionValuesUseCase){
    authenticate {
        post("/nutritionValues/"){
            call.run {
                receiveOrRespond<NutritionValues>()?.let { nutritionValues ->
                    getUserId()?.let { userId ->
                        call.handleResource(
                            resource = saveNutritionValuesUseCase(
                                nutritionValues = nutritionValues,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}