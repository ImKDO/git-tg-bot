package core.dto

import java.time.ZonedDateTime

data class ActionResponse(
    val id: Int?,
    val service: ServicesResponse,
    val method: MethodResponse,
    val chatId: Int,
    val createDate: ZonedDateTime
)
