package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.Resource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.GetProducts
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.configureSearchForProductWithTextRoute(
    getProducts: GetProducts
){
    authenticate {
        get("/{searchText}") {
            val searchText = call.parameters["searchText"]

            if (searchText==null){
                call.respond(HttpStatusCode.BadRequest, message = "Incorrect search text")
                return@get
            }

            val resource = getProducts(searchText)

            if (resource is Resource.Error){
                if (resource.error.exception is NullPointerException){
                    call.respond(HttpStatusCode.NotFound)
                }else{
                    call.respond(HttpStatusCode.BadRequest)
                }
                return@get
            }else{
                call.respond(resource.data!!)
            }
        }
    }
}