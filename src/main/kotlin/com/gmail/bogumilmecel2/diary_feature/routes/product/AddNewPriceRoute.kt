package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewPriceRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.AddNewPriceUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureAddNewPriceRoute(
    addNewPriceUseCase: AddNewPriceUseCase
) {
    authenticate {
        post("/{productId}/prices") {
            call.run {
                getParameter(name = "productId")?.let { productId ->
                    receiveOrRespond<NewPriceRequest>()?.let { newPriceRequest ->
                        handleResource(
                            resource = addNewPriceUseCase(
                                newPriceRequest = newPriceRequest,
                                productId = productId
                            )
                        )
                    }
                }
            }
        }
    }
}