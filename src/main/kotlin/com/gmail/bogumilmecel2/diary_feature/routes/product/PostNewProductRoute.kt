package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getCountryHeader
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.Product
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.InsertProductUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configurePostNewProductRoute(
    insertNewProduct: InsertProductUseCase
) {
    authenticate {
        post {
            call.run {
                receiveOrRespond<Product>()?.let { product ->
                    getUserId()?.let { userId ->
                        getCountryHeader()?.let { country ->
                            handleResource(
                                resource = insertNewProduct(
                                    product = product,
                                    userId = userId,
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