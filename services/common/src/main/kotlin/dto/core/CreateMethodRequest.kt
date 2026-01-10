package boysband.linktracker.dto.core

import kotlin.Long

// No validation annotations per request
data class CreateMethodRequest(
    var serviceId: Long,
    var name: String,
    var describe: String
)

