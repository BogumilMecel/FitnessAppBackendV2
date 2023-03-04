package com.gmail.bogumilmecel2.diary_feature.routes.diary

import com.gmail.bogumilmecel2.common.util.extensions.getUserId
import com.gmail.bogumilmecel2.common.util.extensions.handleResource
import com.gmail.bogumilmecel2.diary_feature.domain.use_case.diary.GetUserCaloriesSum
import com.gmail.bogumilmecel2.diary_feature.resources.CaloriesSum
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Route.configureGetUserCaloriesSumRoute(
    getUserCaloriesSum: GetUserCaloriesSum
){
    authenticate {
        get<CaloriesSum> {caloriesSum ->
            call.run {
                getUserId()?.let { userId ->
                    handleResource(
                        resource = getUserCaloriesSum(
                            date = caloriesSum.date,
                            userId = userId
                        )
                    )
                }
            }
        }
    }
}