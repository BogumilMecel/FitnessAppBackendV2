package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getCountryParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.product.NewProductRequest
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
                receiveOrRespond<NewProductRequest>()?.let { newProductRequest ->
                    getUserId()?.let { userId ->
                        getCountryParameter()?.let { country ->
                            handleResource(
                                resource = insertNewProduct(
                                    newProductRequest = newProductRequest,
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