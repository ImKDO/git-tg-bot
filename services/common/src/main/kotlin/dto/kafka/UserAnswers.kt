package dto.kafka

import netscape.javascript.JSObject
import java.time.ZonedDateTime

data class UserAnswers (
    val chatId: Long,
    val getAnswers: List<Any> = emptyList(),
    val type: EventType,
    val serviceName: String,
    val methodName: String,
    val url: String,
    val newValue: String?,
){
    enum class EventType{
        CONSTANT, PLANNED
    }
}