package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.common.util.extensions.receiveOrRespond
import com.gmail.bogumilmecel2.diary_feature.domain.model.DiaryCacheRequest
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetDiaryCacheUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetDiaryCacheRoute(getDiaryCacheUseCase: GetDiaryCacheUseCase) {
    authenticate {
        post("/diary-cache") {
            call.run {
                getUserId()?.let { userId ->
                    receiveOrRespond<DiaryCacheRequest>()?.let { diaryCacheRequest ->
                        handleResource(
                            resource = getDiaryCacheUseCase(
                                userId = userId,
                                diaryCacheRequest = diaryCacheRequest
                            )
                        )
                    }
                }
            }
        }
    }
}