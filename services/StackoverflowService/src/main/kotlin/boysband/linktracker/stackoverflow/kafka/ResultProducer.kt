package boysband.linktracker.stackoverflow.kafka

import boysband.linktracker.stackoverflow.dto.Record
import boysband.linktracker.dto.kafka.UserAnswers
import boysband.linktracker.dto.kafka.UserRequest
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import kotlin.collections.get

@Service
class ResultProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val logger: Logger
) {
    fun send(event: List<Record>, request: UserRequest, isPlanned: Boolean, isAnswersCheck: Boolean) {
        kafkaTemplate.send(
            "bot-answers",
UserAnswers(
                chatId = request.chatId,
                getAnswers = event,
                type = if (isPlanned) UserAnswers.EventType.PLANNED else UserAnswers.EventType.CONSTANT,
                serviceName = "Stackoverflow",
                methodName = if (isAnswersCheck) "Следить за ответами" else "Следить за комментариями",
                url = request.links[0],
                newValue = ZonedDateTime.now().toString()
            )
        ).whenComplete { result, exception ->
            if(exception != null) {
                logger.error("Error during sending ${exception.message}")
            }
            else{
                logger.info(
                    "Message sent: topic=${result.recordMetadata.topic()} " +
                    "partition=${result.recordMetadata.partition()}"
                )
            }
        }
    }
}