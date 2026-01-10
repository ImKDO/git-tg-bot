package boysband.linktracker.dto.core

import java.time.LocalDateTime

// No validation annotations as requested
data class CreateActionRequest(
    var methodId: Long,
    var tokenId: Long,
    var chatId: Int,
    var serviceId: Long,
    var describe: String,
    var date: LocalDateTime? = null
)
