package boysband.linktracker.service.github

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

data class IssueComment(
    val numberComment: Int,
    val author: String,
    val comment: String
)

data class IssueData(
    val idIssue: Long,
    val totalComments: Int,
    val comments: List<IssueComment>
)

@Service
class Issue(
    private val restClient: RestClient
) {
    private val githubApiBase = "https://api.github.com"

    fun extractIssueInfo(issueUrl: String, token: String? = null): IssueData {
        val (owner, repo, issueNumber) = parseIssueUrl(issueUrl)
        
        val issue = fetchIssue(owner, repo, issueNumber, token)
        val comments = fetchComments(owner, repo, issueNumber, token)
        
        val issueComments = comments.mapIndexed { index, comment ->
            IssueComment(
                numberComment = index + 1,
                author = (comment["user"] as? Map<*, *>)?.get("login") as? String ?: "unknown",
                comment = comment["body"] as? String ?: ""
            )
        }
        
        return IssueData(
            idIssue = issueNumber.toLong(),
            totalComments = issueComments.size,
            comments = issueComments
        )
    }

    private fun parseIssueUrl(url: String): Triple<String, String, String> {
        val regex = Regex("github\\.com/([^/]+)/([^/]+)/issues/(\\d+)")
        val match = regex.find(url) ?: throw IllegalArgumentException("Invalid GitHub issue URL: $url")
        return Triple(match.groupValues[1], match.groupValues[2], match.groupValues[3])
    }

    private fun fetchIssue(owner: String, repo: String, issueNumber: String, token: String?): Map<String, Any> {
        val request = restClient.get()
            .uri("$githubApiBase/repos/$owner/$repo/issues/$issueNumber")
            .header("Accept", "application/vnd.github+json")
        
        token?.let { request.header("Authorization", "Bearer $it") }
        
        @Suppress("UNCHECKED_CAST")
        return request.retrieve().body(Map::class.java) as Map<String, Any>
    }

    private fun fetchComments(owner: String, repo: String, issueNumber: String, token: String?): List<Map<String, Any>> {
        val request = restClient.get()
            .uri("$githubApiBase/repos/$owner/$repo/issues/$issueNumber/comments")
            .header("Accept", "application/vnd.github+json")
        
        token?.let { request.header("Authorization", "Bearer $it") }
        
        @Suppress("UNCHECKED_CAST")
        return request.retrieve().body(List::class.java) as List<Map<String, Any>>
    }
}