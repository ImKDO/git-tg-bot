package boysband.linktracker.dto.kafka

/*
дтошка для процессора, по ней он конструирует ответ и тд
 */
data class LinkUpdateEvent (
    val linkId: Long,
    val url: String,
    val payload: ByteArray,
    val isChanged: Boolean
)