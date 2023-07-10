package com.gmail.bogumilmecel2.user.weight.routes

import com.gmail.bogumilmecel2.user.weight.domain.use_case.WeightUseCases
import io.ktor.server.routing.*

fun Route.configureWeightRoutes(weightUseCases: WeightUseCases){
    route("/weightEntries"){
        configureAddWeightEntryRoute(weightUseCases.addWeightEntryUseCase)
        configureCheckIfShouldAskForWeightDialogsRoute(weightUseCases.checkIfShouldAskForWeightDialogsUseCase)
        configureHandleWeightDialogsAnswerRoute(weightUseCases.handleWeightDialogsAnswerUseCase)
    }
}