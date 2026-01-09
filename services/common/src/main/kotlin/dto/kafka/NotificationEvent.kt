package boysband.linktracker.dto.kafka

/*
дтошка для бота. Бот должен брать шаблон (из кеша или из бд), по нему строить ответ и отправлять
 */
data class NotificationEvent(
    val userId: Long,
    val serviceName: String,
    val methodName: String,
    val data: Map<String, String> = emptyMap(), // Сырые данные, но уже обогощенные/измененные через update processor
    val templateId: String // Update processor выдает шаблон для построение уведомления
)
