package dto.kafka

data class UserAnswers (
    val chatId: Long,
    val getAnswers: List<String> = emptyList(),
    val getSchedulerAnswers: List<String> = emptyList()
)