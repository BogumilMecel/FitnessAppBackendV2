package com.gmail.bogumilmecel2.diary_feature.resources

import com.gmail.bogumilmecel2.common.util.extensions.formatToString
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Resource("")
class DiaryEntries(
    val date:String = Date(System.currentTimeMillis()).formatToString()
)
