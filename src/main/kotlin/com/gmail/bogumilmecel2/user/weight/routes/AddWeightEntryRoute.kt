package com.gmail.bogumilmecel2.user.weight.routes

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.use_case.AddWeightEntryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureAddWeightEntryRoute(addWeightEntryUseCase: AddWeightEntryUseCase) {
    authenticate {
        post("") {
            call.run {
                receiveOrRespond<WeightEntry>()?.let { weightEntry ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = addWeightEntryUseCase(
                                value = weightEntry.value,
                                date = weightEntry.date,
                                userId = userId,
                            )
                        )
                    }
                }
            }
        }
    }
}