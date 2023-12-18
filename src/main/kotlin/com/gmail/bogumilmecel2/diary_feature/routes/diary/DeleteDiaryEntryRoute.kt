package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.DeleteDiaryEntryRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.DeleteDiaryEntryUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureDeleteDiaryEntryRoute(deleteDiaryEntryUseCase: DeleteDiaryEntryUseCase) {
    authenticate {
        delete {
            call.run {
                receiveOrRespond<DeleteDiaryEntryRequest>()?.let { deleteDiaryEntryRequest ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = deleteDiaryEntryUseCase(
                                deleteDiaryEntryRequest = deleteDiaryEntryRequest,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}