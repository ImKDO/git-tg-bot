package boysband.linktracker.core.mapper

import boysband.linktracker.core.model.HistoryAnswer
import boysband.linktracker.dto.core.HistoryAnswerDto
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class HistoryAnswerMapper {
    fun toDto(entity: HistoryAnswer): HistoryAnswerDto = HistoryAnswerDto(
        id = entity.id,
        chatId = entity.chatId,
        response = entity.response,
        date = entity.date
    )

    fun toEntity(dto: HistoryAnswerDto): HistoryAnswer = HistoryAnswer(
        id = dto.id,
        chatId = dto.chatId,
        response = dto.response,
        date = dto.date ?: LocalDateTime.now()
    )
}