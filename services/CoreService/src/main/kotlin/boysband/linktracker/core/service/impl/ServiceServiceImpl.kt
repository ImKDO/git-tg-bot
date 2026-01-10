package boysband.linktracker.core.service.impl

import boysband.linktracker.core.exception.HistoryAnswerNotFoundException
import boysband.linktracker.dto.core.CreateServiceRequest
import boysband.linktracker.dto.core.UpdateServiceRequest
import boysband.linktracker.dto.core.NewService
import boysband.linktracker.core.mapper.ServiceMapper
import boysband.linktracker.core.repository.ServiceRepository
import boysband.linktracker.core.service.ServiceService
import org.springframework.stereotype.Service as SpringService
import org.springframework.transaction.annotation.Transactional

@SpringService
open class ServiceServiceImpl(
    private val repository: ServiceRepository,
    private val mapper: ServiceMapper
) : ServiceService {

    @Transactional
    override fun create(request: CreateServiceRequest): NewService {
        val entity = mapper.fromCreateRequest(request)
        val saved = repository.save(entity)
        return mapper.toDto(saved)
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): NewService {
        val entity = repository.findById(id).orElseThrow { HistoryAnswerNotFoundException("Service with id=$id not found") }
        return mapper.toDto(entity)
    }

    @Transactional(readOnly = true)
    override fun getAll(): List<NewService> = repository.findAll().map { mapper.toDto(it) }

    @Transactional
    override fun update(id: Long, request: UpdateServiceRequest): NewService {
        val entity = repository.findById(id).orElseThrow { HistoryAnswerNotFoundException("Service with id=$id not found") }
        val updated = mapper.applyUpdate(entity, request)
        val saved = repository.save(updated)
        return mapper.toDto(saved)
    }

    @Transactional
    override fun delete(id: Long) {
        if (!repository.existsById(id)) throw HistoryAnswerNotFoundException("Service with id=$id not found")
        repository.deleteById(id)
    }
}
