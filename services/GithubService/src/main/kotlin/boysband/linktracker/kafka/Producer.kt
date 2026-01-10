package boysband.linktracker.kafka

import dto.kafka.UserAnswers
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class Producer(
    private val kafkaTemplate: KafkaTemplate<String, UserAnswers>
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun sendAnswer(topic: String, answer: UserAnswers) {
        log.info("Sending answer to topic: {} for chatId: {}", topic, answer.chatId)
        kafkaTemplate.send(topic, answer)
    }
}
