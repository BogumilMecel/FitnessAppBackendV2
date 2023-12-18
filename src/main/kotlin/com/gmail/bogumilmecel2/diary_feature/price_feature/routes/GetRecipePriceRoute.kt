package com.gmail.bogumilmecel2.diary_feature.price_feature.routes

import com.gmail.bogumilmecel2.common.util.extensions.getCountryHeader
import com.gmail.bogumilmecel2.common.util.extensions.getCurrencyHeader
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.model.RecipePriceRequest
import com.gmail.bogumilmecel2.diary_feature.price_feature.domain.use_cases.GetRecipePriceUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetRecipePriceRoute(
    getRecipePrice: GetRecipePriceUseCase
) {
    authenticate {
        post("/price") {
            call.run {
                getCurrencyHeader()?.let { currency ->
                    getCountryHeader()?.let { country ->
                        receiveOrRespond<RecipePriceRequest>()?.let { request ->
                            handleResource(
                                resource = getRecipePrice(
                                    currency = currency,
                                    ingredients = request.ingredients,
                                    country = country
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}