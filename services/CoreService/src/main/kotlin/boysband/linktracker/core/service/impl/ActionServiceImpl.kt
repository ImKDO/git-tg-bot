package boysband.linktracker.core.service.impl

import boysband.linktracker.core.exception.HistoryAnswerNotFoundException
import boysband.linktracker.core.mapper.ActionMapper
import boysband.linktracker.core.repository.ActionRepository
import boysband.linktracker.core.service.ActionService
import boysband.linktracker.dto.core.ActionDto
import boysband.linktracker.dto.core.CreateActionRequest
import boysband.linktracker.dto.core.UpdateActionRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class ActionServiceImpl(
    private val repository: ActionRepository,
    private val mapper: ActionMapper
) : ActionService {

    @Transactional
    override fun create(request: CreateActionRequest): ActionDto {
        val entity = mapper.fromCreateRequest(request)
        val saved = repository.save(entity)
        return mapper.toDto(saved)
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): ActionDto {
        val entity = repository.findById(id).orElseThrow { HistoryAnswerNotFoundException("Action with id=$id not found") }
        return mapper.toDto(entity)
    }

    @Transactional(readOnly = true)
    override fun getAll(): List<ActionDto> = repository.findAll().map { mapper.toDto(it) }

    @Transactional
    override fun update(id: Long, request: UpdateActionRequest): ActionDto {
        val entity = repository.findById(id).orElseThrow { HistoryAnswerNotFoundException("Action with id=$id not found") }
        val updated = mapper.applyUpdate(entity, request)
        val saved = repository.save(updated)
        return mapper.toDto(saved)
    }

    @Transactional
    override fun delete(id: Long) {
        if (!repository.existsById(id)) throw HistoryAnswerNotFoundException("Action with id=$id not found")
        repository.deleteById(id)
    }
}
