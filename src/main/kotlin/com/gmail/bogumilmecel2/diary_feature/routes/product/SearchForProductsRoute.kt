package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.domain.constants.Constants.Api.PAGE
import com.gmail.bogumilmecel2.common.domain.constants.Constants.Api.SEARCH_TEXT
import com.gmail.bogumilmecel2.common.util.extensions.getCountryHeader
import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.GetProductsUseCase
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureSearchForProductWithTextRoute(
    getProductsUseCase: GetProductsUseCase
){
    authenticate {
        get {
            call.run {
                getParameter(SEARCH_TEXT)?.let { searchText ->
                    getCountryHeader()?.let { country ->
                        handleResource(
                            resource = getProductsUseCase(
                                searchText = searchText,
                                country = country,
                                currency = country.getCurrency(),
                                page = getParameter(PAGE)?.toIntOrNull() ?: 1
                            )
                        )
                    }
                }
            }
        }
    }
}