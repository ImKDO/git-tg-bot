package boysband.linktracker.dto.kafka

/*
дтошка для бота. Ему должно приходить что то читаемое, потом он уже размечает и отправит юзеру
 */
data class NotificationEvent(
    val userId: Long,
    val message: String,
    val title: String,
    val linkUrl: String
)
