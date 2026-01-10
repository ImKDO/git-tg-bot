package dto.kafka

import java.time.ZonedDateTime


data class UserRequest (
    val chatId: Long,
    val service: String = "",
    val token: String? = null,
    val action: String = "",
    val links: List<String> = emptyList(),
    val lastValue: String?
)

