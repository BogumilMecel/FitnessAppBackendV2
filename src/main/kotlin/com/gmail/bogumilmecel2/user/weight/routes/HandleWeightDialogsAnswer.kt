package com.gmail.bogumilmecel2.user.weight.routes

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightDialogsRequest
import com.gmail.bogumilmecel2.user.weight.domain.use_case.HandleWeightDialogsAnswerUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureHandleWeightDialogsAnswerRoute(
    handleWeightDialogsAnswerUseCase: HandleWeightDialogsAnswerUseCase
) {
    authenticate {
        post("/dialogs") {
            call.run {
                getUserId()?.let { userId ->
                    receiveOrRespond<WeightDialogsRequest>()?.let { weightDialogsRequest ->
                        handleResource(
                            resource = handleWeightDialogsAnswerUseCase(
                                userId = userId,
                                weightDialogsRequest = weightDialogsRequest
                            )
                        )
                    }
                }
            }
        }
    }
}