package `is`.apiservice.service

import `is`.apiservice.dto.CommentDataset
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Сервис для экспорта датасета комментариев в JSON файл
 */
@Service
class DatasetExportService(
    private val objectMapper: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Экспортирует датасет в JSON файл
     * @param dataset список комментариев
     * @param issueRange диапазон issue для имени файла
     * @return путь к созданному файлу
     */
    fun exportToJson(dataset: List<CommentDataset>, issueRange: String): String {
        try {
            // Создаем директорию для датасетов если её нет
            val datasetDir = File("datasets")
            if (!datasetDir.exists()) {
                datasetDir.mkdirs()
                log.info("Создана директория для датасетов: {}", datasetDir.absolutePath)
            }

            // Формируем имя файла с временной меткой
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            val fileName = "github_comments_${issueRange}_${timestamp}.json"
            val file = File(datasetDir, fileName)

            // Настраиваем ObjectMapper для красивого форматирования
            val writer = objectMapper.writerWithDefaultPrettyPrinter()

            // Создаем структуру для JSON
            val jsonData = mapOf(
                "metadata" to mapOf(
                    "exportTime" to LocalDateTime.now().toString(),
                    "issueRange" to issueRange,
                    "totalComments" to dataset.size,
                    "totalIssues" to dataset.map { it.numberIssue }.distinct().size
                ),
                "comments" to dataset
            )

            // Записываем в файл
            writer.writeValue(file, jsonData)

            log.info("✅ Датасет успешно экспортирован в файл: {}", file.absolutePath)
            log.info("   Размер файла: {} KB", file.length() / 1024)

            return file.absolutePath
        } catch (e: Exception) {
            log.error("❌ Ошибка при экспорте датасета в JSON: ${e.message}", e)
            throw e
        }
    }

    /**
     * Экспортирует датасет, сгруппированный по issue
     */
    fun exportGroupedByIssue(dataset: List<CommentDataset>, issueRange: String): String {
        try {
            val datasetDir = File("datasets")
            if (!datasetDir.exists()) {
                datasetDir.mkdirs()
            }

            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
            val fileName = "github_comments_grouped_${issueRange}_${timestamp}.json"
            val file = File(datasetDir, fileName)

            // Группируем по issue
            val groupedData = dataset.groupBy { it.numberIssue }.map { (issueNumber, comments) ->
                mapOf(
                    "issueNumber" to issueNumber,
                    "commentsCount" to comments.size,
                    "comments" to comments
                )
            }

            val jsonData = mapOf(
                "metadata" to mapOf(
                    "exportTime" to LocalDateTime.now().toString(),
                    "issueRange" to issueRange,
                    "totalComments" to dataset.size,
                    "totalIssues" to groupedData.size
                ),
                "issues" to groupedData
            )

            val writer = objectMapper.writerWithDefaultPrettyPrinter()
            writer.writeValue(file, jsonData)

            log.info("✅ Датасет (сгруппированный) успешно экспортирован в файл: {}", file.absolutePath)
            log.info("   Размер файла: {} KB", file.length() / 1024)

            return file.absolutePath
        } catch (e: Exception) {
            log.error("❌ Ошибка при экспорте сгруппированного датасета в JSON: ${e.message}", e)
            throw e
        }
    }
}

