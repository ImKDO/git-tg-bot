package dto.core

import core.dto.ServicesResponse

data class MethodResponse(
    val id: Int,
    val service: ServicesResponse,
    val name: String,
    val describe: String
)