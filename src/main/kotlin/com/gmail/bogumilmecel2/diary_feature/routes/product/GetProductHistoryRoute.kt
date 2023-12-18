package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.GetProductHistoryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetProductHistoryRoute(
    getProductHistoryUseCase: GetProductHistoryUseCase
) {
    authenticate {
        get("/history") {
            call.run {
                getUserId()?.let { userId ->
                    handleResource(
                        resource = getProductHistoryUseCase(userId = userId)
                    )
                }
            }
        }
    }
}