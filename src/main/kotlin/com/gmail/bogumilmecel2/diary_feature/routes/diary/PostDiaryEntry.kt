package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.InsertProductDiaryEntryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configurePostDiaryEntryRoute(
    insertProductDiaryEntryUseCase: InsertProductDiaryEntryUseCase
) {
    authenticate {
        post("/product") {
            call.run {
                receiveOrRespond<ProductDiaryEntryPostRequest>()?.let { productDiaryEntryPostRequest ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = insertProductDiaryEntryUseCase(
                                productDiaryEntryPostRequest = productDiaryEntryPostRequest,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}