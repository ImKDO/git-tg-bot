package boysband.linktracker.core.service

import boysband.linktracker.dto.core.CreateHistoryAnswerRequest
import boysband.linktracker.dto.core.HistoryAnswerDto

interface HistoryAnswerService {
    fun create(request: CreateHistoryAnswerRequest): HistoryAnswerDto
    fun getById(id: Long): HistoryAnswerDto
    fun getAll(): List<HistoryAnswerDto>
    fun delete(id: Long)
}

