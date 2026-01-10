package boysband.linktracker.kafka

import boysband.linktracker.service.GithubProcessorService
import dto.kafka.UserRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class Consumer(
    private val githubProcessorService: GithubProcessorService,
    private val producer: Producer
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val RESPONSE_TOPIC = "github_response"
    }

    @KafkaListener(topics = ["task_github"], groupId = "github-service")
    fun listenUser(request: UserRequest) {
        log.info("Github: Received request for chatId: {}, action: {}", request.chatId, request.action)

        try {
            val answer = githubProcessorService.processRequest(request)
            producer.sendAnswer(RESPONSE_TOPIC, answer)
            log.info("Github: Successfully processed request for chatId: {}", request.chatId)
        } catch (e: Exception) {
            log.error("Github: Error processing request for chatId: {}", request.chatId, e)
        }
    }
}