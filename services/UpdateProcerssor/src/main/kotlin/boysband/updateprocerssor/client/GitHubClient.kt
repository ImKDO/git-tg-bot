package boysband.updateprocerssor.client

import boysband.updateprocerssor.dto.github.GitHubCommit
import boysband.updateprocerssor.dto.github.GitHubIssue
import boysband.updateprocerssor.dto.github.GitHubWorkflowRun
import boysband.updateprocerssor.dto.github.GitHubWorkflowRuns
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Instant

@Component
class GitHubClient {
    private val logger = LoggerFactory.getLogger(GitHubClient::class.java)
    
    companion object {
        private const val GITHUB_API_BASE = "https://api.github.com"
    }
    
    private fun buildClient(token: String?): RestClient {
        val builder = RestClient.builder()
            .baseUrl(GITHUB_API_BASE)
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
        
        token?.let {
            builder.defaultHeader("Authorization", "Bearer $it")
        }
        
        return builder.build()
    }
    
    /**
     * Получает issues из репозитория, обновленные после указанной даты
     */
    fun getIssues(owner: String, repo: String, token: String?, since: Instant?): List<GitHubIssue> {
        logger.info("Fetching issues for $owner/$repo since $since")
        
        val client = buildClient(token)
        var uri = "/repos/$owner/$repo/issues?state=all&sort=updated&direction=desc&per_page=100"
        since?.let { uri += "&since=${it}" }
        
        return try {
            client.get()
                .uri(uri)
                .retrieve()
                .body(object : ParameterizedTypeReference<List<GitHubIssue>>() {}) ?: emptyList()
        } catch (e: Exception) {
            logger.error("Failed to fetch issues for $owner/$repo", e)
            emptyList()
        }
    }
    
    /**
     * Получает коммиты из репозитория, созданные после указанной даты
     */
    fun getCommits(owner: String, repo: String, token: String?, since: Instant?): List<GitHubCommit> {
        logger.info("Fetching commits for $owner/$repo since $since")
        
        val client = buildClient(token)
        var uri = "/repos/$owner/$repo/commits?per_page=100"
        since?.let { uri += "&since=${it}" }
        
        return try {
            client.get()
                .uri(uri)
                .retrieve()
                .body(object : ParameterizedTypeReference<List<GitHubCommit>>() {}) ?: emptyList()
        } catch (e: Exception) {
            logger.error("Failed to fetch commits for $owner/$repo", e)
            emptyList()
        }
    }
    
    /**
     * Получает workflow runs (actions) из репозитория
     */
    fun getWorkflowRuns(owner: String, repo: String, token: String?, since: Instant?): List<GitHubWorkflowRun> {
        logger.info("Fetching workflow runs for $owner/$repo since $since")
        
        val client = buildClient(token)
        var uri = "/repos/$owner/$repo/actions/runs?per_page=100"
        since?.let { uri += "&created=>$it" }
        
        return try {
            val response = client.get()
                .uri(uri)
                .retrieve()
                .body(GitHubWorkflowRuns::class.java)
            
            response?.workflowRuns?.filter { run ->
                since == null || run.updatedAt.isAfter(since)
            } ?: emptyList()
        } catch (e: Exception) {
            logger.error("Failed to fetch workflow runs for $owner/$repo", e)
            emptyList()
        }
    }
    
    /**
     * Парсит URL GitHub репозитория и извлекает owner и repo
     */
    fun parseRepoUrl(url: String): Pair<String, String>? {
        // Поддержка форматов:
        // https://github.com/owner/repo
        // https://github.com/owner/repo.git
        // github.com/owner/repo
        val regex = Regex("""(?:https?://)?github\.com/([^/]+)/([^/]+?)(?:\.git)?/?$""")
        val match = regex.find(url.trim())
        return match?.let {
            Pair(it.groupValues[1], it.groupValues[2])
        }
    }
}
