package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.ProductDiaryEntryPostRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.InsertDiaryEntry
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configurePostDiaryEntryRoute(
    insertDiaryEntry: InsertDiaryEntry
) {
    authenticate {
        post("/product") {
            call.run {
                receiveOrRespond<ProductDiaryEntryPostRequest>()?.let { productDiaryEntryPostRequest ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = insertDiaryEntry(
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