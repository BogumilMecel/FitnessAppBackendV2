package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.diary_entry.EditProductDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.EditProductDiaryEntryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureEditProductDiaryEntryRoute(
    editProductDiaryEntryUseCase: EditProductDiaryEntryUseCase
) {
    authenticate {
        put("/product") {
            call.run {
                receiveOrRespond<EditProductDiaryEntryRequest>()?.let { editProductDiaryEntryRequest ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = editProductDiaryEntryUseCase(
                                editProductDiaryEntryRequest = editProductDiaryEntryRequest,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}