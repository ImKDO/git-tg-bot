package boysband.linktracker.dto.core

import java.time.LocalDateTime

data class CreateHistoryAnswerRequest(
    var chatId: Int,
    var response: String,
    var date: LocalDateTime? = null
)