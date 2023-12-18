package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getCountryParameter
import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.GetProductsUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureSearchForProductWithTextRoute(
    getProductsUseCase: GetProductsUseCase
){
    authenticate {
        get("/{searchText}") {
            call.run {
                getParameter("searchText")?.let { searchText ->
                    getCountryParameter()?.let { country ->
                        handleResource(
                            resource = getProductsUseCase(
                                searchText = searchText,
                                country = country,
                                currency = country.getCurrency()
                            )
                        )
                    }
                }
            }
        }
    }
}