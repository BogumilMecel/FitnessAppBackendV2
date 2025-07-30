package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetProductRoute(getProductUseCase: GetProductUseCase) {
    authenticate {
        get("/{product_id}") {
            call.handleRequest {
                getProductUseCase(productId = call.getParameter("product_id"))
            }
        }
    }
}