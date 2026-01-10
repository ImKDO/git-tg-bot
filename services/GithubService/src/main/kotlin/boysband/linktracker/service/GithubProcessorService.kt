package boysband.linktracker.service

import boysband.linktracker.service.github.Action
import boysband.linktracker.service.github.Commit
import boysband.linktracker.service.github.Issue
import boysband.linktracker.dto.kafka.UserAnswers
import boysband.linktracker.dto.kafka.UserRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GithubProcessorService(
    private val issue: Issue,
    private val commit: Commit,
    private val action: Action
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val SERVICE_NAME = "github"
        const val ACTION_ISSUE = "issue"
        const val ACTION_COMMIT = "commit"
        const val ACTION_ACTIONS = "actions"
    }

    fun processRequest(request: UserRequest): UserAnswers {
        log.info("Processing request for chatId: {}, action: {}", request.chatId, request.action)

        val answers = mutableListOf<Any>()
        val methodName = request.action

        request.links.forEach { link ->
            try {
                val result = when (request.action.lowercase()) {
                    ACTION_ISSUE -> processIssue(link, request.token)
                    ACTION_COMMIT -> processCommit(link, request.token)
                    ACTION_ACTIONS -> processActions(link, request.token)
                    else -> mapOf("error" to "Unknown action: ${request.action}")
                }
                answers.add(result)
            } catch (e: Exception) {
                log.error("Error processing link: {} for action: {}", link, request.action, e)
                answers.add(mapOf("error" to e.message, "link" to link))
            }
        }

        return UserAnswers(
            chatId = request.chatId,
            getAnswers = answers.map { it as Object },
            type = UserAnswers.EventType.CONSTANT,
            serviceName = SERVICE_NAME,
            methodName = methodName,
            url = request.links.firstOrNull() ?: "",
            newValue = null
        )
    }

    private fun processIssue(link: String, token: String?): Map<String, Any> {
        val issueData = issue.extractIssueInfo(link, token)
        return mapOf(
            "id_issue" to issueData.idIssue,
            "total_comments" to issueData.totalComments,
            "comments" to issueData.comments.map { comment ->
                mapOf(
                    "number_comment" to comment.numberComment,
                    "author" to comment.author,
                    "comment" to comment.comment
                )
            }
        )
    }

    private fun processCommit(link: String, token: String?): Map<String, Any> {
        val commitHistory = commit.extractCommitHistory(link, token)
        return mapOf(
            "repository" to commitHistory.repository,
            "total_commits" to commitHistory.totalCommits,
            "commits" to commitHistory.commits.map { c ->
                mapOf(
                    "sha" to c.sha,
                    "author" to c.author,
                    "message" to c.message,
                    "branch" to c.branch,
                    "date" to c.date
                )
            }
        )
    }

    private fun processActions(link: String, token: String?): Map<String, Any> {
        val actionHistory = action.extractLatestActions(link, 10, token)
        return mapOf(
            "repository" to actionHistory.repository,
            "total_runs" to actionHistory.totalRuns,
            "workflow_runs" to actionHistory.workflowRuns.map { run ->
                mapOf(
                    "id" to run.id,
                    "name" to run.name,
                    "status" to run.status,
                    "conclusion" to (run.conclusion ?: ""),
                    "branch" to run.branch,
                    "event" to run.event,
                    "created_at" to run.createdAt,
                    "actor" to run.actor,
                    "html_url" to run.htmlUrl
                )
            }
        )
    }
}
