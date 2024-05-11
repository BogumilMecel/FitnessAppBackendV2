package com.gmail.bogumilmecel2.user.user_data.routes

import com.gmail.bogumilmecel2.diary_feature.routes.diary.*
import com.gmail.bogumilmecel2.user.user_data.domain.use_cases.UserDataUseCases
import io.ktor.server.routing.*

fun Route.configureUserDataRoutes(userDataUseCases: UserDataUseCases) {
    route("/userData/") {
        configureSaveUserNutritionValuesRoute(userDataUseCases.saveNutritionValuesUseCase)
        configureHandleUserInformationRoute(userDataUseCases.handleUserInformationUseCase)
        configureGetUserProductDiaryEntriesRoute(userDataUseCases.getUserProductDiaryEntriesUseCase)
        configureGetUserRecipeDiaryEntriesRoute(userDataUseCases.getUserRecipeDiaryEntriesUseCase)
        configureGetUserProductsRoute(userDataUseCases.getUserProductsUseCase)
        configureGetUserRecipesRoute(userDataUseCases.getUserRecipesUseCase)
        configureGetDiaryCacheRoute(userDataUseCases.getDiaryCacheUseCase)
    }
}