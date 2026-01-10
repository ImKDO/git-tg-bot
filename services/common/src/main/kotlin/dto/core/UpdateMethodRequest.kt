package boysband.linktracker.dto.core

// No validation annotations per request
data class UpdateMethodRequest(
    var serviceId: Long,
    var name: String,
    var describe: String
)

