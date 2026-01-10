package boysband.linktracker.core.mapper

import boysband.linktracker.core.model.Service
import boysband.linktracker.dto.core.CreateServiceRequest
import org.springframework.stereotype.Component
import boysband.linktracker.dto.core.NewService
import boysband.linktracker.dto.core.UpdateServiceRequest

@Component
class ServiceMapper {
    fun toDto(entity: Service): NewService = NewService(link = entity.link)
    fun toEntity(dto: NewService): Service = Service(id = dto.hashCode().toLong(), link = dto.link)
    fun fromCreateRequest(req: CreateServiceRequest): Service = Service(id = null, link = req.link)
    fun applyUpdate(entity: Service, req: UpdateServiceRequest): Service {
        entity.link = req.link
        return entity
    }
}

