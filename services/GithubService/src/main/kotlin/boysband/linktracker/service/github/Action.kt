package boysband.linktracker.service.github

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

data class WorkflowRunInfo(
    val id: Long,
    val name: String,
    val status: String,
    val conclusion: String?,
    val branch: String,
    val event: String,
    val createdAt: String,
    val updatedAt: String,
    val actor: String,
    val htmlUrl: String
)

data class ActionHistory(
    val repository: String,
    val totalRuns: Int,
    val workflowRuns: List<WorkflowRunInfo>
)

@Service
class Action(
    private val restClient: RestClient
) {
    private val githubApiBase = "https://api.github.com"

    fun extractActionHistory(repoUrl: String, token: String? = null): ActionHistory {
        val (owner, repo) = parseRepoUrl(repoUrl)
        val runs = fetchWorkflowRuns(owner, repo, token)
        
        val workflowRuns = runs.map { run ->
            val actor = run["actor"] as? Map<*, *>
            
            WorkflowRunInfo(
                id = (run["id"] as? Number)?.toLong() ?: 0,
                name = run["name"] as? String ?: "",
                status = run["status"] as? String ?: "",
                conclusion = run["conclusion"] as? String,
                branch = run["head_branch"] as? String ?: "",
                event = run["event"] as? String ?: "",
                createdAt = run["created_at"] as? String ?: "",
                updatedAt = run["updated_at"] as? String ?: "",
                actor = actor?.get("login") as? String ?: "unknown",
                htmlUrl = run["html_url"] as? String ?: ""
            )
        }
        
        return ActionHistory(
            repository = "$owner/$repo",
            totalRuns = workflowRuns.size,
            workflowRuns = workflowRuns
        )
    }

    fun extractLatestActions(repoUrl: String, limit: Int = 10, token: String? = null): ActionHistory {
        val history = extractActionHistory(repoUrl, token)
        return history.copy(
            totalRuns = minOf(history.totalRuns, limit),
            workflowRuns = history.workflowRuns.take(limit)
        )
    }

    fun extractActionsByStatus(repoUrl: String, status: String, token: String? = null): ActionHistory {
        val (owner, repo) = parseRepoUrl(repoUrl)
        val runs = fetchWorkflowRunsByStatus(owner, repo, status, token)
        
        val workflowRuns = runs.map { run ->
            val actor = run["actor"] as? Map<*, *>
            
            WorkflowRunInfo(
                id = (run["id"] as? Number)?.toLong() ?: 0,
                name = run["name"] as? String ?: "",
                status = run["status"] as? String ?: "",
                conclusion = run["conclusion"] as? String,
                branch = run["head_branch"] as? String ?: "",
                event = run["event"] as? String ?: "",
                createdAt = run["created_at"] as? String ?: "",
                updatedAt = run["updated_at"] as? String ?: "",
                actor = actor?.get("login") as? String ?: "unknown",
                htmlUrl = run["html_url"] as? String ?: ""
            )
        }
        
        return ActionHistory(
            repository = "$owner/$repo",
            totalRuns = workflowRuns.size,
            workflowRuns = workflowRuns
        )
    }

    private fun parseRepoUrl(url: String): Pair<String, String> {
        val regex = Regex("github\\.com/([^/]+)/([^/]+)")
        val match = regex.find(url) ?: throw IllegalArgumentException("Invalid GitHub repository URL: $url")
        return Pair(match.groupValues[1], match.groupValues[2].removeSuffix(".git"))
    }

    private fun fetchWorkflowRuns(owner: String, repo: String, token: String?): List<Map<String, Any>> {
        val request = restClient.get()
            .uri("$githubApiBase/repos/$owner/$repo/actions/runs?per_page=30")
            .header("Accept", "application/vnd.github+json")
        
        token?.let { request.header("Authorization", "Bearer $it") }
        
        @Suppress("UNCHECKED_CAST")
        val response = request.retrieve().body(Map::class.java) as Map<String, Any>
        @Suppress("UNCHECKED_CAST")
        return response["workflow_runs"] as? List<Map<String, Any>> ?: emptyList()
    }

    private fun fetchWorkflowRunsByStatus(owner: String, repo: String, status: String, token: String?): List<Map<String, Any>> {
        val request = restClient.get()
            .uri("$githubApiBase/repos/$owner/$repo/actions/runs?status=$status&per_page=30")
            .header("Accept", "application/vnd.github+json")
        
        token?.let { request.header("Authorization", "Bearer $it") }
        
        @Suppress("UNCHECKED_CAST")
        val response = request.retrieve().body(Map::class.java) as Map<String, Any>
        @Suppress("UNCHECKED_CAST")
        return response["workflow_runs"] as? List<Map<String, Any>> ?: emptyList()
    }
}