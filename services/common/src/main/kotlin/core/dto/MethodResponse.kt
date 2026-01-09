package core.dto

data class MethodResponse(
    val id: Int,
    val service: ServicesResponse,
    val name: String,
    val describe: String
)