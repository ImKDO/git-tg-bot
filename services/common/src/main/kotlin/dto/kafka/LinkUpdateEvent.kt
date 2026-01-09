package boysband.linktracker.dto.kafka

/*
дтошка для процессора, которую он обрабатывает
 */
data class LinkUpdateEvent (
    val linkId: Long,
    val url: String,
    val methodId: String,
    val data: Map<String, String> = emptyMap(), // Сырые данные
)