package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getCountryHeader
import com.gmail.bogumilmecel2.common.util.extensions.getCurrencyHeader
import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetProductPriceUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetProductPriceRoute(getProductPriceUseCase: GetProductPriceUseCase) {
    authenticate {
        get("/{productId}/price") {
            call.run {
                getCountryHeader()?.let { country ->
                    getCurrencyHeader()?.let { currency ->
                        getParameter("productId")?.let { productId ->
                            handleResource(
                                resource = getProductPriceUseCase(
                                    currency = currency,
                                    country = country,
                                    productId = productId
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}