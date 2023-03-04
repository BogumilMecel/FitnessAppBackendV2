package com.gmail.bogumilmecel2.diary_feature.routes.product

import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.product.SearchForProductWithBarcode
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureSearchForProductWithBarcodeRoute(
    searchForProductWithBarcode: SearchForProductWithBarcode
){
    get("/{barcode}") {
        call.run {
            getParameter("barcode")?.let { barcode ->
                getUserId()?.let {
                    handleResource(
                        resource = searchForProductWithBarcode(barcode)
                    )
                }
            }
        }
    }
}