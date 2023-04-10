package com.gmail.bogumilmecel2.diary_feature.price_feature.routes

import com.gmail.bogumilmecel2.common.util.extensions.*
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewPriceRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.AddNewPriceUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureAddNewPriceRoute(
    addNewPriceUseCase: AddNewPriceUseCase
) {
    authenticate {
        post("/{productId}/price") {
            call.run {
                getParameter(name = "productId")?.let { productId ->
                    getCountryHeader()?.let { country ->
                        getCurrencyHeader()?.let { currency ->
                            receiveOrRespond<NewPriceRequest>()?.let { newPriceRequest ->
                                handleResource(
                                    resource = addNewPriceUseCase(
                                        newPriceRequest = newPriceRequest,
                                        productId = productId,
                                        country = country,
                                        currency = currency
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}