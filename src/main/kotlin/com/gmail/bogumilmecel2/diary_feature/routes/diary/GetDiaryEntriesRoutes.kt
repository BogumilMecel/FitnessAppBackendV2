package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getDateParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetDiaryEntriesUseCase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureGetDiaryEntriesRoute(getDiaryEntriesUseCase: GetDiaryEntriesUseCase) {
    authenticate {
        get {
            call.run {
                getUserId()?.let { userId ->
                    getDateParameter()?.let { localDate ->
                        call.handleResource(
                            resource = getDiaryEntriesUseCase(
                                date = localDate,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}