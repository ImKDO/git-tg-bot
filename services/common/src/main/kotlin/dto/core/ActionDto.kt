package boysband.linktracker.dto.core

import java.time.LocalDateTime

data class ActionDto(
    val id: Long? = null,
    val methodId: Long,
    val tokenId: Long,
    val chatId: Int,
    val serviceId: Long,
    val describe: String,
    val date: LocalDateTime
)

