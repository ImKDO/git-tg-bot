package boysband.updateprocerssor.service

import boysband.linktracker.dto.kafka.UserAnswers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

@Service
class DiffService {
    private val logger = LoggerFactory.getLogger(DiffService::class.java)
    
    /**
     * Фильтрует данные в UserAnswers, оставляя только записи после lastValue
     */
    fun filterByDate(answers: UserAnswers, lastValue: String?): UserAnswers? {
        if (lastValue.isNullOrBlank()) {
            logger.debug("No lastValue provided for chatId=${answers.chatId}, returning all data")
            return answers
        }
        
        val lastUpdate = parseLastValue(lastValue)
        if (lastUpdate == null) {
            logger.warn("Failed to parse lastValue for chatId=${answers.chatId}, returning all data")
            return answers
        }
        
        logger.debug("Filtering data for chatId=${answers.chatId} since $lastUpdate")
        
        val filteredAnswers = answers.getAnswers.filter { item ->
            when (item) {
                is Map<*, *> -> {
                    // Ищем поле с датой (updated_at, created_at, date)
                    val dateFields = listOf("updated_at", "created_at", "date")
                    val itemDate = dateFields.firstNotNullOfOrNull { field ->
                        item[field]?.toString()?.let { parseLastValue(it) }
                    }
                    
                    if (itemDate != null) {
                        itemDate.isAfter(lastUpdate)
                    } else {
                        // Если нет даты - включаем элемент
                        logger.warn("No date field found in item for chatId=${answers.chatId}, including item")
                        true
                    }
                }
                else -> {
                    logger.warn("Unexpected item type ${item?.let { it::class.java.simpleName }} for chatId=${answers.chatId}")
                    true
                }
            }
        }
        
        if (filteredAnswers.isEmpty()) {
            logger.info("No new data for chatId=${answers.chatId} after filtering")
            return null
        }
        
        logger.info("Filtered ${answers.getAnswers.size} -> ${filteredAnswers.size} items for chatId=${answers.chatId}")
        
        return answers.copy(getAnswers = filteredAnswers)
    }
    
    private fun parseLastValue(lastValue: String): Instant? {
        return try {
            Instant.parse(lastValue)
        } catch (e: DateTimeParseException) {
            try {
                ZonedDateTime.parse(lastValue).toInstant()
            } catch (e2: DateTimeParseException) {
                logger.debug("Failed to parse date: $lastValue")
                null
            }
        }
    }
}
