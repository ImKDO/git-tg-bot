package boysband.updateprocerssor.kafka

import dto.kafka.UserAnswers
import boysband.updateprocerssor.service.DiffService
import boysband.updateprocerssor.service.LastValueCache
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class RawDataConsumer(
    private val lastValueCache: LastValueCache,
    private val diffService: DiffService,
    private val updateAnswerProducer: UpdateAnswerProducer
) {
    private val logger = LoggerFactory.getLogger(RawDataConsumer::class.java)
    
    @KafkaListener(
        topics = ["\${kafka.topics.github-raw-data}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun consumeRawData(rawAnswers: UserAnswers) {
        logger.info("Received raw data: chatId=${rawAnswers.chatId}, method=${rawAnswers.methodName}, items=${rawAnswers.getAnswers.size}")
        
        try {
            val lastValue = lastValueCache.get(rawAnswers.chatId)
            
            val filteredAnswers = diffService.filterByDate(rawAnswers, lastValue)
            
            if (filteredAnswers != null) {
                updateAnswerProducer.sendAnswer(filteredAnswers)
                logger.info("Sent filtered answer for chatId=${filteredAnswers.chatId}, items=${filteredAnswers.getAnswers.size}")
            } else {
                logger.info("No new data to send for chatId=${rawAnswers.chatId}")
            }
        } catch (e: Exception) {
            logger.error("Error processing raw data for chatId=${rawAnswers.chatId}", e)
        }
    }
}
