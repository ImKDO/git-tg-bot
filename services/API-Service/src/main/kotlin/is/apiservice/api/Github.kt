package `is`.apiservice.api

import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import `is`.apiservice.dto.JsonGithubIssueComments
import `is`.apiservice.dto.CommentDataset

@Service
class Github(
    restTemplateBuilder: RestTemplateBuilder
) {
    private val restTemplate: RestTemplate = restTemplateBuilder.build()
    private val log = LoggerFactory.getLogger(javaClass)

    fun getIssueComments(linkIssue: String, token: String): List<CommentDataset> {
        log.info("Начинаем сбор комментариев из issue")

        val startIssueNumber = extractIssueNumber(linkIssue)
        val baseUrl = extractBaseUrl(linkIssue)
        val endIssueNumber = startIssueNumber + 500

        log.info("Будем обрабатывать issue от {} до {}", startIssueNumber, endIssueNumber)

        val headers = HttpHeaders().apply {
            setBearerAuth(token)
            set("Accept", "application/vnd.github.v3+json")
        }
        val request = HttpEntity<String>(headers)

        val dataset = mutableListOf<CommentDataset>()

        try {
            for (currentIssueNumber in startIssueNumber..endIssueNumber) {
                val issueUrl = "$baseUrl/issues/$currentIssueNumber"

                try {
                    // Проверяем существование issue
                    restTemplate.exchange<String>(
                        issueUrl,
                        HttpMethod.GET,
                        request
                    )

                    // Если issue существует, получаем комментарии
                    val commentsResponse = restTemplate.exchange<String>(
                        "$issueUrl/comments",
                        HttpMethod.GET,
                        request
                    )
                    val commentsJson = commentsResponse.body

                    val mapper = jacksonObjectMapper()
                    val comments: List<JsonGithubIssueComments> = mapper.readValue(commentsJson!!)

                    log.info("Issue #{}: получено {} комментариев", currentIssueNumber, comments.size)

                    comments.forEachIndexed { index, comment ->
                        val datasetEntry = CommentDataset(
                            numberIssue = currentIssueNumber,
                            numberCommentInIssue = index + 1,
                            authorComment = comment.user.login,
                            commentText = comment.body
                        )
                        dataset.add(datasetEntry)
                    }

                } catch (e: Exception) {
                    // Issue может не существовать (404) - это нормально, пропускаем
                    log.debug("Issue #{} недоступен или не существует: {}", currentIssueNumber, e.message)
                }
            }

            log.info("Датасет создан, всего записей: {}", dataset.size)
            log.info("Обработано issue с {} по {}", startIssueNumber, endIssueNumber)

        } catch (e: Exception) {
            log.error("Критическая ошибка при запросе к Github: ${e.message}", e)
        }

        return dataset
    }

    /**
     * Извлекает номер issue из URL
     * Примеры:
     * - https://api.github.com/repos/owner/repo/issues/123 -> 123
     * - https://github.com/owner/repo/issues/123 -> 123
     */
    private fun extractIssueNumber(linkIssue: String): Long {
        val regex = """/issues/(\d+)""".toRegex()
        val match = regex.find(linkIssue)
        return match?.groupValues?.get(1)?.toLongOrNull() ?: 0L
    }

    /**
     * Извлекает базовый URL репозитория из URL issue
     * Примеры:
     * - https://api.github.com/repos/owner/repo/issues/123 -> https://api.github.com/repos/owner/repo
     * - https://github.com/owner/repo/issues/123 -> https://api.github.com/repos/owner/repo
     */
    private fun extractBaseUrl(linkIssue: String): String {
        // Если это уже API URL
        if (linkIssue.contains("api.github.com/repos/")) {
            val regex = """(https://api\.github\.com/repos/[^/]+/[^/]+)""".toRegex()
            val match = regex.find(linkIssue)
            return match?.groupValues?.get(1) ?: linkIssue
        }
        // Если это web URL, конвертируем в API URL
        val regex = """https://github\.com/([^/]+)/([^/]+)""".toRegex()
        val match = regex.find(linkIssue)
        return if (match != null) {
            val owner = match.groupValues[1]
            val repo = match.groupValues[2]
            "https://api.github.com/repos/$owner/$repo"
        } else {
            linkIssue
        }
    }

    fun getIssue(linkIssue: String, token: String) {
        log.info("Прослушиваем issue: {}", linkIssue)

        val headers = HttpHeaders().apply {
            setBearerAuth(token)
            set("Accept", "application/vnd.github.v3+json")
        }

        val request = HttpEntity<String>(headers)

        try {
            val response = restTemplate.exchange<String>(
                linkIssue,
                HttpMethod.GET,
                request
            )
            log.info("Получили вот такую инфу:\n{}", response.body)
        } catch (e: Exception) {
            log.error("Ошибка при запросе к Github issue: ${e.message}", e)
        }
    }
}