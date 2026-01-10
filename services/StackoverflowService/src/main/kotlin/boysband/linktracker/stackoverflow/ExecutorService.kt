package boysband.linktracker.boysband.linktracker.stackoverflow

import boysband.linktracker.boysband.linktracker.stackoverflow.dto.Record
import boysband.linktracker.boysband.linktracker.stackoverflow.kafka.ResultProducer
import boysband.linktracker.dto.kafka.UserRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ExecutorService(
    private val client: ClientService,
    private val producer: ResultProducer
) {

    suspend fun execute(task: UserRequest) {
        var records: List<Record>
        var isAnswer = false
        when (task.action ) {
            "answers" -> {
                records = client.getNewAnswers(task.links[0], task.lastValue)
                isAnswer = true
            }
            "comments" -> records = client.getNewAnswersAndComments(task.links[0], task.lastValue)
            else -> throw IllegalArgumentException()
        }

        if (!records.isEmpty()) {
            producer.send(records, task, true, isAnswer);
        }
    }
}