package boysband.linktracker.dto.kafka

/*
дтошка для клиентов
 */
data class LinkUpdateTask (
    val linkId: Long,
    val url: String,
)
