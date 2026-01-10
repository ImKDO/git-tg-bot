package boysband.updateprocerssor.kafka

import dto.kafka.UserAnswers
import dto.kafka.UserRequest
import boysband.updateprocerssor.service.UpdateProcessorService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UpdateRequestConsumer(
    private val updateProcessorService: UpdateProcessorService,
    private val updateAnswerProducer: UpdateAnswerProducer
) {
    private val logger = LoggerFactory.getLogger(UpdateRequestConsumer::class.java)
    
    @KafkaListener(
        topics = ["\${kafka.topics.user-requests}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun consumeUserRequest(request: UserRequest) {
        logger.info("Received user request: chatId=${request.chatId}, service=${request.service}, action=${request.action}")
        
        try {
            val answers: List<UserAnswers> = updateProcessorService.processRequest(request)
            
            if (answers.isNotEmpty()) {
                answers.forEach { answer ->
                    updateAnswerProducer.sendAnswer(answer)
                    logger.info("Sent answer for chatId=${answer.chatId}, method=${answer.methodName}, items=${answer.getAnswers.size}")
                }
            } else {
                logger.info("No new updates found for chatId=${request.chatId}")
            }
        } catch (e: Exception) {
            logger.error("Error processing request for chatId=${request.chatId}", e)
        }
    }
}
