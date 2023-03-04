package com.gmail.bogumilmecel2.user.weight.routes

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.user.weight.domain.model.WeightEntry
import com.gmail.bogumilmecel2.user.weight.domain.use_case.AddWeightEntry
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureAddWeightEntryRoute(addWeightEntry: AddWeightEntry) {
    authenticate {
        post("") {
            call.run {
                receiveOrRespond<WeightEntry>()?.let { entry ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = addWeightEntry(
                                weightEntry = entry,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}