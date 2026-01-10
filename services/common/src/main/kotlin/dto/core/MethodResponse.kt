package boysband.linktracker.dto.core

import boysband.linktracker.dto.core.ServicesResponse

data class MethodResponse(
    val id: Int,
    val service: ServicesResponse,
    val name: String,
    val describe: String
)