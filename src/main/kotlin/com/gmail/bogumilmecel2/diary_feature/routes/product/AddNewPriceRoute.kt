package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewPriceRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.AddNewPrice
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureAddNewPriceRoute(
    addNewPrice: AddNewPrice
) {
    authenticate {
        post("/{productId}/prices") {
            call.run {
                getParameter(name = "productId")?.let { productId ->
                    receiveOrRespond<NewPriceRequest>()?.let { newPriceRequest ->
                        handleResource(
                            resource = addNewPrice(
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