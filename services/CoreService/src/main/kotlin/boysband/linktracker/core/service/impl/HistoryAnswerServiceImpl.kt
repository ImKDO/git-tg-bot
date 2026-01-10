package boysband.linktracker.core.service.impl

import boysband.linktracker.core.exception.HistoryAnswerNotFoundException
import boysband.linktracker.core.mapper.HistoryAnswerMapper
import boysband.linktracker.core.model.HistoryAnswer
import boysband.linktracker.core.repository.HistoryAnswerRepository
import boysband.linktracker.core.service.HistoryAnswerService
import boysband.linktracker.dto.core.CreateHistoryAnswerRequest
import boysband.linktracker.dto.core.HistoryAnswerDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
open class HistoryAnswerServiceImpl(
    private val repository: HistoryAnswerRepository,
    private val mapper: HistoryAnswerMapper
) : HistoryAnswerService {

    @Transactional
    override fun create(request: CreateHistoryAnswerRequest): HistoryAnswerDto {
        val entity = HistoryAnswer(
            id = null,
            chatId = request.chatId,
            response = request.response,
            date = request.date ?: LocalDateTime.now()
        )
        val saved = repository.save(entity)
        return mapper.toDto(saved)
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): HistoryAnswerDto {
        val entity = repository.findById(id).orElseThrow { HistoryAnswerNotFoundException("HistoryAnswer with id=$id not found") }
        return mapper.toDto(entity)
    }

    @Transactional(readOnly = true)
    override fun getAll(): List<HistoryAnswerDto> {
        return repository.findAll().map { mapper.toDto(it) }
    }

    @Transactional
    override fun delete(id: Long) {
        if (!repository.existsById(id)) throw HistoryAnswerNotFoundException("HistoryAnswer with id=$id not found")
        repository.deleteById(id)
    }
}
