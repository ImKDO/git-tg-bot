package boysband.linktracker.core.mapper

import boysband.linktracker.core.model.Method
import boysband.linktracker.core.repository.ServiceRepository
import boysband.linktracker.dto.core.CreateMethodRequest
import boysband.linktracker.dto.core.MethodDto
import boysband.linktracker.dto.core.UpdateMethodRequest
import org.springframework.stereotype.Component

@Component
class MethodMapper(private val serviceRepository: ServiceRepository) {
    fun toDto(entity: Method): MethodDto = MethodDto(
        id = entity.id,
        serviceId = entity.service.id ?: throw RuntimeException("Service id is null"),
        name = entity.name,
        describe = entity.describe
    )

    fun fromCreateRequest(req: CreateMethodRequest): Method {
        val svc = serviceRepository.findById(req.serviceId).orElseThrow { RuntimeException("Service with id=${req.serviceId} not found") }
        return Method(id = null, service = svc, name = req.name, describe = req.describe)
    }

    fun applyUpdate(entity: Method, req: UpdateMethodRequest): Method {
        val svc = serviceRepository.findById(req.serviceId).orElseThrow { RuntimeException("Service with id=${req.serviceId} not found") }
        entity.service = svc
        entity.name = req.name
        entity.describe = req.describe
        return entity
    }
}
