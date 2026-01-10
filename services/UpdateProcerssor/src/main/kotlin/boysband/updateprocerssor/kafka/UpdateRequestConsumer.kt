package boysband.updateprocerssor.kafka

import dto.kafka.UserRequest
import boysband.updateprocerssor.service.LastValueCache
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UpdateRequestConsumer(
    private val lastValueCache: LastValueCache
) {
    private val logger = LoggerFactory.getLogger(UpdateRequestConsumer::class.java)
    
    @KafkaListener(
        topics = ["\${kafka.topics.user-requests}"],
        groupId = "\${spring.kafka.consumer.group-id}"
    )
    fun consumeUserRequest(request: UserRequest) {
        logger.info("Received user request: chatId=${request.chatId}, service=${request.service}, action=${request.action}")
        
        lastValueCache.put(request.chatId, request.lastValue)
        
        logger.debug("Cached lastValue=${request.lastValue} for chatId=${request.chatId}")
    }
}
