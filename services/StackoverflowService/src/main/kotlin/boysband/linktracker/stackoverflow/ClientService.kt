package boysband.linktracker.stackoverflow

import boysband.linktracker.stackoverflow.dto.Record
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


//TODO: добавить обработку ошибок
@Service
class ClientService(
    private val client: WebClient,
    private val mapper: ObjectMapper
) {

    suspend fun getNewAnswers(link: String, fromDate: String?): List<Record> {
        val fromDate = if (fromDate != null) ZonedDateTime.parse(fromDate).toEpochSecond() else 0
        val id = """/questions/(\d+)/""".toRegex().find(link)?.groups?.get(1)?.value
        val additionUri = "/${id}?fromdate=${fromDate}&order=asc&sort=creation&site=stackoverflow&filter=!*Mg4Pjfm.dOZh)cH"
        val request = client
            .method(HttpMethod.GET)
            .uri(additionUri)
            .headers { headers ->
                headers.add("User-Agent", "LinkTracker")
            }

        try{
            val body = mapper.readTree(request.retrieve().awaitBody<String>())
            val result = ArrayList<Record>()

            body["items"].forEach { item ->
                item["answers"]?.forEach {answer ->
                    result.add(
                        Record(
                            author = answer["owner"]["display_name"].asText(),
                            text = answer["body"].asText(),
                            creationDate = ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(answer["creation_date"].asLong()),
                                ZoneId.of("UTC")
                            ),
                        )
                    )
                }
            }

            return result
        }
        catch(ex: Exception){
            println(ex.message)
            return listOf<Record>()
        }
    }

    suspend fun getNewAnswersAndComments(link: String, fromDate: String?): List<Record> {
        val fromDate = if (fromDate != null) ZonedDateTime.parse(fromDate).toEpochSecond() else 0
        val id = """/questions/(\d+)/""".toRegex().find(link)?.groups?.get(1)?.value
        val additionUri = "/${id}?fromdate=${fromDate}&order=asc&sort=creation&site=stackoverflow&filter=!T3AudphlMFjTE*ozcm"
        val request = client
            .method(HttpMethod.GET)
            .uri(additionUri)
            .headers { headers ->
                headers.add("User-Agent", "LinkTracker")
            }

        try{
            val body = mapper.readTree(request.retrieve().awaitBody<String>())
            val result = ArrayList<Record>()

            body["items"].forEach { item ->
                item["comments"]?.forEach {comment ->
                    result.add(
                        Record(
                            author = comment["owner"]["display_name"].asText(),
                            text = comment["body"].asText(),
                            creationDate = ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(comment["creation_date"].asLong()),
                                ZoneId.of("UTC")
                            ),
                        )
                    )
                }
            }

            return result
        }
        catch(ex: Exception){
            println(ex.message)
            return listOf<Record>()
        }
    }
}
