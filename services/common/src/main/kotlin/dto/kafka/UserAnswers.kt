package dto.kafka

import com.fasterxml.jackson.annotation.JsonProperty


data class UserAnswers (
    @JsonProperty("chatId")
    val chatId: Long,

    @JsonProperty("get_answers")
    val getAnswers: List<Any> = emptyList(),
    @JsonProperty("type")
    val type: EventType,
    @JsonProperty("serviceName")
    val serviceName: String,
    @JsonProperty("method_name")
    val methodName: String,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("newValue")
    val newValue: String,
){
    enum class EventType{
        CONSTANT, PLANNED
    }
}