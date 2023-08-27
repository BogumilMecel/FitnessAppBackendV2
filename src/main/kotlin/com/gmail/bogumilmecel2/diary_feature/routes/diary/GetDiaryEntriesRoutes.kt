package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetDiaryEntries
import com.gmail.bogumilmecel2.diary_feature.resources.DiaryEntries
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Route.configureGetDiaryEntriesRoute(getDiaryEntries: GetDiaryEntries) {
    authenticate {
        get<DiaryEntries> { diaryEntry ->
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getDiaryEntries(
                            date = diaryEntry.date,
                            userId = userId
                        )
                    )
                }
            }
        }
    }
}

fun Route.configureGetDiaryEntriesExperimentalRoute(getDiaryEntries: GetDiaryEntries) {
    authenticate {
        get("experimental") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getDiaryEntries(
                            userId = userId,
                            complete = false
                        )
                    )
                }
            }
        }
    }
}

fun Route.configureGetDiaryEntriesCompleteRoute(getDiaryEntries: GetDiaryEntries) {
    authenticate {
        get("complete") {
            call.run {
                getUserId()?.let { userId ->
                    call.handleResource(
                        resource = getDiaryEntries(
                            userId = userId,
                            complete = true
                        )
                    )
                }
            }
        }
    }
}