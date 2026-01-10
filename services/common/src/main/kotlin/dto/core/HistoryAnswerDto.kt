package boysband.linktracker.dto.core

import java.time.LocalDateTime

data class HistoryAnswerDto(
    val id: Long? = null,
    val chatId: Int,
    val response: String,
    val date: LocalDateTime? = null
)