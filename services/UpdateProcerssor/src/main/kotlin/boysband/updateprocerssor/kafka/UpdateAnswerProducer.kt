package boysband.updateprocerssor.kafka

import boysband.linktracker.dto.kafka.UserAnswers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UpdateAnswerProducer(
    private val kafkaTemplate: KafkaTemplate<String, UserAnswers>
) {
    private val logger = LoggerFactory.getLogger(UpdateAnswerProducer::class.java)
    
    @Value("\${kafka.topics.user-answers}")
    private lateinit var answersTopic: String
    
    fun sendAnswer(answer: UserAnswers) {
        try {
            kafkaTemplate.send(answersTopic, answer.chatId.toString(), answer)
                .whenComplete { result, ex ->
                    if (ex == null) {
                        logger.debug("Sent answer to topic $answersTopic: chatId=${answer.chatId}")
                    } else {
                        logger.error("Failed to send answer to topic $answersTopic", ex)
                    }
                }
        } catch (e: Exception) {
            logger.error("Error sending answer to Kafka", e)
            throw e
        }
    }
}
