package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getParameter
import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.DeleteDiaryEntry
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.configureDeleteDiaryEntryRoute(
    deleteDiaryEntry: DeleteDiaryEntry
){
    authenticate {
        delete("/{diaryEntryId}") {
            call.run {
                getParameter("diaryEntryId")?.let { diaryEntryId ->
                    getUserId()?.let { userId ->
                        handleResource(
                            resource = deleteDiaryEntry(
                                diaryEntryId = diaryEntryId,
                                userId = userId
                            )
                        )
                    }
                }
            }
        }
    }
}