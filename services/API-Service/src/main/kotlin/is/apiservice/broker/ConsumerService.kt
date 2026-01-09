package `is`.apiservice.broker

import `is`.apiservice.api.Github
import `is`.apiservice.dto.TaskUserToken
import `is`.apiservice.service.DatasetExportService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ConsumerService(
    private val github: Github,
    private val datasetExportService: DatasetExportService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["task_user_token"], groupId = "api-service")
    fun listenTokenUser(taskUserToken: TaskUserToken) {
        try {
            log.info("Прослушиваем task_user_token")
            log.info("\nПолучены данные: \n{}", taskUserToken)

            val token = taskUserToken.token
            val linkIssue = taskUserToken.linkIssue

            if (token != null && linkIssue != null) {
                // Получаем датасет комментариев
                val dataset = github.getIssueComments(linkIssue, token)

                if (dataset.isNotEmpty()) {
                    // Определяем диапазон issue
                    val minIssue = dataset.minOfOrNull { it.numberIssue } ?: 0
                    val maxIssue = dataset.maxOfOrNull { it.numberIssue } ?: 0
                    val issueRange = "${minIssue}-${maxIssue}"

                    log.info("\n=== ДАТАСЕТ КОММЕНТАРИЕВ (сгруппирован по Issue) ===")

                    // Группируем по номеру issue
                    val groupedByIssue = dataset.groupBy { it.numberIssue }

                    groupedByIssue.forEach { (issueNumber, comments) ->
                        log.info("\n--- Issue #{} ({} комментариев) ---", issueNumber, comments.size)
                        comments.forEach { entry ->
                            log.info(
                                "  <<{}, {}, {}, \"{}\">>",
                                entry.numberIssue,
                                entry.numberCommentInIssue,
                                entry.authorComment,
                                entry.commentText.replace("\n", " ").take(100) +
                                if (entry.commentText.length > 100) "..." else ""
                            )
                        }
                    }

                    log.info("\n=== ИТОГО: {} комментариев из {} issue ===",
                        dataset.size,
                        groupedByIssue.size
                    )

                    // Экспортируем в JSON (обычный)
                    val flatFilePath = datasetExportService.exportToJson(dataset, issueRange)
                    log.info("\n📄 Плоский датасет сохранен: {}", flatFilePath)

                    // Экспортируем в JSON (сгруппированный)
                    val groupedFilePath = datasetExportService.exportGroupedByIssue(dataset, issueRange)
                    log.info("📄 Сгруппированный датасет сохранен: {}\n", groupedFilePath)

                } else {
                    log.warn("Датасет пуст, комментарии не найдены")
                }

            } else {
                log.warn("Токен или ссылка на issue отсутствуют в сообщении: $taskUserToken")
            }

        } catch (e: Exception) {
            log.error("Ошибочка получилась: ${e.message}", e)
        }
    }
}