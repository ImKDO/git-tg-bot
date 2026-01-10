package boysband.linktracker.core.service.impl

import boysband.linktracker.core.exception.HistoryAnswerNotFoundException
import boysband.linktracker.core.mapper.MethodMapper
import boysband.linktracker.core.repository.MethodRepository
import boysband.linktracker.core.service.MethodService
import boysband.linktracker.dto.core.CreateMethodRequest
import boysband.linktracker.dto.core.UpdateMethodRequest
import boysband.linktracker.dto.core.MethodDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class MethodServiceImpl(
    private val repository: MethodRepository,
    private val mapper: MethodMapper
) : MethodService {

    @Transactional
    override fun create(request: CreateMethodRequest): MethodDto {
        val entity = mapper.fromCreateRequest(request)
        val saved = repository.save(entity)
        return mapper.toDto(saved)
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): MethodDto {
        val entity = repository.findById(id).orElseThrow { HistoryAnswerNotFoundException("Method with id=$id not found") }
        return mapper.toDto(entity)
    }

    @Transactional(readOnly = true)
    override fun getAll(): List<MethodDto> = repository.findAll().map { mapper.toDto(it) }

    @Transactional
    override fun update(id: Long, request: UpdateMethodRequest): MethodDto {
        val entity = repository.findById(id).orElseThrow { HistoryAnswerNotFoundException("Method with id=$id not found") }
        val updated = mapper.applyUpdate(entity, request)
        val saved = repository.save(updated)
        return mapper.toDto(saved)
    }

    @Transactional
    override fun delete(id: Long) {
        if (!repository.existsById(id)) throw HistoryAnswerNotFoundException("Method with id=$id not found")
        repository.deleteById(id)
    }
}
