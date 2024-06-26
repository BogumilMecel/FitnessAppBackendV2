package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getDateTimeParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.GetUserProductsUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetUserProductsRoute(getUserProductsUseCase: GetUserProductsUseCase) {
    authenticate {
        get("/products") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getUserProductsUseCase(
                            userId = userId,
                            latestDateTime = getDateTimeParameter()
                        )
                    )
                }
            }
        }
    }
}