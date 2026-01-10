package boysband.updateprocerssor.service

import boysband.updateprocerssor.client.GitHubClient
import dto.kafka.UserAnswers
import dto.kafka.UserRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZonedDateTime

@Service
class UpdateProcessorService(
    private val gitHubClient: GitHubClient
) {
    private val logger = LoggerFactory.getLogger(UpdateProcessorService::class.java)
    
    companion object {
        const val SERVICE_GITHUB = "github"
        
        const val ACTION_ISSUES = "issues"
        const val ACTION_COMMITS = "commits"
        const val ACTION_ACTIONS = "actions"
    }
    
    /**
     * Обрабатывает запрос пользователя и возвращает список ответов с новыми изменениями
     */
    fun processRequest(request: UserRequest): List<UserAnswers> {
        logger.info("Processing request for chatId: ${request.chatId}, service: ${request.service}, action: ${request.action}")
        
        if (request.service.lowercase() != SERVICE_GITHUB) {
            logger.warn("Unsupported service: ${request.service}")
            return emptyList()
        }
        
        val lastUpdate = getLastUpdate(request)
        val results = mutableListOf<UserAnswers>()
        
        for (link in request.links) {
            val repoInfo = gitHubClient.parseRepoUrl(link)
            if (repoInfo == null) {
                logger.warn("Failed to parse GitHub URL: $link")
                continue
            }
            
            val (owner, repo) = repoInfo
            
            val answer = when (request.action.lowercase()) {
                ACTION_ISSUES -> processIssues(request, owner, repo, link, lastUpdate)
                ACTION_COMMITS -> processCommits(request, owner, repo, link, lastUpdate)
                ACTION_ACTIONS -> processActions(request, owner, repo, link, lastUpdate)
                else -> {
                    logger.warn("Unknown action: ${request.action}")
                    null
                }
            }
            
            answer?.let { results.add(it) }
        }
        
        return results
    }
    
    private fun processIssues(
        request: UserRequest,
        owner: String,
        repo: String,
        url: String,
        lastUpdate: Instant?
    ): UserAnswers? {
        val issues = gitHubClient.getIssues(owner, repo, request.token, lastUpdate)
        
        if (issues.isEmpty()) {
            logger.info("No new issues for $owner/$repo")
            return null
        }
        
        // Фильтруем только те issues, которые были обновлены после lastUpdate
        val filteredIssues = if (lastUpdate != null) {
            issues.filter { it.updatedAt.isAfter(lastUpdate) }
        } else {
            issues
        }
        
        if (filteredIssues.isEmpty()) {
            return null
        }
        
        logger.info("Found ${filteredIssues.size} new/updated issues for $owner/$repo")
        
        return UserAnswers(
            chatId = request.chatId,
            getAnswers = filteredIssues.map { issue ->
                mapOf(
                    "id" to issue.id,
                    "number" to issue.number,
                    "title" to issue.title,
                    "state" to issue.state,
                    "body" to (issue.body ?: ""),
                    "html_url" to issue.htmlUrl,
                    "author" to (issue.user?.login ?: "unknown"),
                    "labels" to issue.labels.map { it.name },
                    "assignees" to issue.assignees.map { it.login },
                    "created_at" to issue.createdAt.toString(),
                    "updated_at" to issue.updatedAt.toString()
                )
            },
            type = UserAnswers.EventType.PLANNED,
            serviceName = SERVICE_GITHUB,
            methodName = ACTION_ISSUES,
            url = url
        )
    }
    
    private fun processCommits(
        request: UserRequest,
        owner: String,
        repo: String,
        url: String,
        lastUpdate: Instant?
    ): UserAnswers? {
        val commits = gitHubClient.getCommits(owner, repo, request.token, lastUpdate)
        
        if (commits.isEmpty()) {
            logger.info("No new commits for $owner/$repo")
            return null
        }
        
        // Фильтруем коммиты после lastUpdate
        val filteredCommits = if (lastUpdate != null) {
            commits.filter { commit ->
                commit.commit.author?.date?.isAfter(lastUpdate) == true ||
                commit.commit.committer?.date?.isAfter(lastUpdate) == true
            }
        } else {
            commits
        }
        
        if (filteredCommits.isEmpty()) {
            return null
        }
        
        logger.info("Found ${filteredCommits.size} new commits for $owner/$repo")
        
        return UserAnswers(
            chatId = request.chatId,
            getAnswers = filteredCommits.map { commit ->
                mapOf(
                    "sha" to commit.sha,
                    "message" to commit.commit.message,
                    "author_name" to (commit.commit.author?.name ?: "unknown"),
                    "author_email" to (commit.commit.author?.email ?: ""),
                    "date" to (commit.commit.author?.date?.toString() ?: ""),
                    "html_url" to (commit.htmlUrl ?: ""),
                    "committer" to (commit.commit.committer?.name ?: "")
                )
            },
            type = UserAnswers.EventType.PLANNED,
            serviceName = SERVICE_GITHUB,
            methodName = ACTION_COMMITS,
            url = url
        )
    }
    
    private fun processActions(
        request: UserRequest,
        owner: String,
        repo: String,
        url: String,
        lastUpdate: Instant?
    ): UserAnswers? {
        val runs = gitHubClient.getWorkflowRuns(owner, repo, request.token, lastUpdate)
        
        if (runs.isEmpty()) {
            logger.info("No new workflow runs for $owner/$repo")
            return null
        }
        
        logger.info("Found ${runs.size} new workflow runs for $owner/$repo")
        
        return UserAnswers(
            chatId = request.chatId,
            getAnswers = runs.map { run ->
                mapOf(
                    "id" to run.id,
                    "name" to (run.name ?: ""),
                    "status" to run.status,
                    "conclusion" to (run.conclusion ?: ""),
                    "branch" to (run.headBranch ?: ""),
                    "event" to run.event,
                    "run_number" to run.runNumber,
                    "html_url" to run.htmlUrl,
                    "created_at" to run.createdAt.toString(),
                    "updated_at" to run.updatedAt.toString(),
                    "actor" to (run.actor?.login ?: "")
                )
            },
            type = UserAnswers.EventType.PLANNED,
            serviceName = SERVICE_GITHUB,
            methodName = ACTION_ACTIONS,
            url = url
        )
    }
    
    private fun getLastUpdate(request: UserRequest): Instant? {
        if (request.lastValue.isNullOrBlank()) {
            return null
        }
        
        return try {
            Instant.parse(request.lastValue)
        } catch (e: Exception) {
            try {
                ZonedDateTime.parse(request.lastValue).toInstant()
            } catch (e2: Exception) {
                logger.warn("Failed to parse lastValue: ${request.lastValue}", e2)
                null
            }
        }
    }
}
