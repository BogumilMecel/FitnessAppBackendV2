package com.gmail.bogumilmecel2.user.user_data.routes

import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.UserDataUseCases
import io.ktor.server.routing.*

fun Route.configureUserDataRoutes(userDataUseCases: UserDataUseCases) {
    route("/userData/") {
        configureSaveUserNutritionValuesRoute(userDataUseCases.saveNutritionValuesUseCase)
        configureHandleUserInformationRoute(userDataUseCases.handleUserInformationUseCase)
        configureGetUserDiaryItemsRoute(userDataUseCases.getUserDiaryItemsUseCase)
    }
}