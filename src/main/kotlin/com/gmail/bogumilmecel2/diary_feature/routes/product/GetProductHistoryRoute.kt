package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.GetProductHistory
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetProductHistoryRoute(
    getProductHistory: GetProductHistory
) {
    authenticate {
        get("/history") {
            call.run {
                getUserId()?.let { userId ->
                    handleResource(
                        resource = getProductHistory(userId = userId)
                    )
                }
            }
        }
    }
}