package boysband.linktracker.boysband.linktracker.stackoverflow.kafka

import boysband.linktracker.boysband.linktracker.stackoverflow.ClientService
import boysband.linktracker.boysband.linktracker.stackoverflow.ExecutorService
import boysband.linktracker.dto.kafka.UserRequest
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TaskConsumer(
    private val logger: Logger,
    private val executorService: ExecutorService,
) {

    @KafkaListener(
        topics = ["stackoverflow-task"],
        groupId = "my-group",
    )
    fun consume(task: UserRequest) = runBlocking{
        logger.info("Consuming task from ${task.chatId}")
        executorService.execute(task)
    }
}