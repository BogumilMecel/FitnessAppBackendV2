package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.common.GetProductUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetProductRoute(getProductUseCase: GetProductUseCase) {
    authenticate {
        get("/product/{productId}") {
            call.run {
                getParameter("productId")?.let { productId ->
                    handleResource(
                        resource = getProductUseCase(
                            productId = productId
                        )
                    )
                }
            }
        }
    }
}