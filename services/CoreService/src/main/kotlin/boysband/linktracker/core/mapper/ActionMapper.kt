package boysband.linktracker.core.mapper

import boysband.linktracker.core.model.Action
import boysband.linktracker.core.repository.MethodRepository
import boysband.linktracker.core.repository.ServiceRepository
import boysband.linktracker.core.repository.TokenRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import boysband.linktracker.dto.core.CreateActionRequest
import boysband.linktracker.dto.core.UpdateActionRequest
import boysband.linktracker.dto.core.ActionDto

@Component
class ActionMapper(
    private val methodRepository: MethodRepository,
    private val tokenRepository: TokenRepository,
    private val serviceRepository: ServiceRepository
) {
    fun toDto(entity: Action): ActionDto = ActionDto(
        id = entity.id,
        methodId = entity.method.id ?: throw RuntimeException("Method id is null"),
        tokenId = entity.token.id ?: throw RuntimeException("Token id is null"),
        chatId = entity.chatId,
        serviceId = entity.service.id ?: throw RuntimeException("Service id is null"),
        describe = entity.describe,
        date = entity.date
    )

    fun fromCreateRequest(req: CreateActionRequest): Action {
        val method = methodRepository.findById(req.methodId).orElseThrow { RuntimeException("Method not found") }
        val token = tokenRepository.findById(req.tokenId).orElseThrow { RuntimeException("Token not found") }
        val service = serviceRepository.findById(req.serviceId).orElseThrow { RuntimeException("Service not found") }
        return Action(
            id = null,
            method = method,
            token = token,
            chatId = req.chatId,
            service = service,
            describe = req.describe,
            date = req.date ?: LocalDateTime.now()
        )
    }

    fun applyUpdate(entity: Action, req: UpdateActionRequest): Action {
        val method = methodRepository.findById(req.methodId).orElseThrow { RuntimeException("Method not found") }
        val token = tokenRepository.findById(req.tokenId).orElseThrow { RuntimeException("Token not found") }
        val service = serviceRepository.findById(req.serviceId).orElseThrow { RuntimeException("Service not found") }
        entity.method = method
        entity.token = token
        entity.chatId = req.chatId
        entity.service = service
        entity.describe = req.describe
        entity.date = req.date ?: entity.date
        return entity
    }
}
