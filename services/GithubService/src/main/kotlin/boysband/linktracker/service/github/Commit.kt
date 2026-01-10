package boysband.linktracker.service.github

import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

data class CommitInfo(
    val sha: String,
    val author: String,
    val message: String,
    val branch: String,
    val date: String
)

data class CommitHistory(
    val repository: String,
    val totalCommits: Int,
    val commits: List<CommitInfo>
)

@Service
class Commit(
    private val restClient: RestClient
) {
    private val githubApiBase = "https://api.github.com"

    fun extractCommitHistory(repoUrl: String, token: String? = null, branch: String = "main"): CommitHistory {
        val (owner, repo) = parseRepoUrl(repoUrl)
        
        val branches = fetchBranches(owner, repo, token)
        val allCommits = mutableListOf<CommitInfo>()
        
        branches.forEach { branchInfo ->
            val branchName = branchInfo["name"] as? String ?: return@forEach
            val commits = fetchCommits(owner, repo, branchName, token)
            
            commits.forEach { commit ->
                val commitData = commit["commit"] as? Map<*, *>
                val authorData = commitData?.get("author") as? Map<*, *>
                
                allCommits.add(
                    CommitInfo(
                        sha = commit["sha"] as? String ?: "",
                        author = authorData?.get("name") as? String ?: "unknown",
                        message = commitData?.get("message") as? String ?: "",
                        branch = branchName,
                        date = authorData?.get("date") as? String ?: ""
                    )
                )
            }
        }
        
        return CommitHistory(
            repository = "$owner/$repo",
            totalCommits = allCommits.size,
            commits = allCommits.distinctBy { it.sha }
        )
    }

    fun extractCommitsFromBranch(repoUrl: String, branch: String, token: String? = null): CommitHistory {
        val (owner, repo) = parseRepoUrl(repoUrl)
        val commits = fetchCommits(owner, repo, branch, token)
        
        val commitInfos = commits.map { commit ->
            val commitData = commit["commit"] as? Map<*, *>
            val authorData = commitData?.get("author") as? Map<*, *>
            
            CommitInfo(
                sha = commit["sha"] as? String ?: "",
                author = authorData?.get("name") as? String ?: "unknown",
                message = commitData?.get("message") as? String ?: "",
                branch = branch,
                date = authorData?.get("date") as? String ?: ""
            )
        }
        
        return CommitHistory(
            repository = "$owner/$repo",
            totalCommits = commitInfos.size,
            commits = commitInfos
        )
    }

    private fun parseRepoUrl(url: String): Pair<String, String> {
        val regex = Regex("github\\.com/([^/]+)/([^/]+)")
        val match = regex.find(url) ?: throw IllegalArgumentException("Invalid GitHub repository URL: $url")
        return Pair(match.groupValues[1], match.groupValues[2].removeSuffix(".git"))
    }

    private fun fetchBranches(owner: String, repo: String, token: String?): List<Map<String, Any>> {
        val request = restClient.get()
            .uri("$githubApiBase/repos/$owner/$repo/branches")
            .header("Accept", "application/vnd.github+json")
        
        token?.let { request.header("Authorization", "Bearer $it") }
        
        @Suppress("UNCHECKED_CAST")
        return request.retrieve().body(List::class.java) as List<Map<String, Any>>
    }

    private fun fetchCommits(owner: String, repo: String, branch: String, token: String?): List<Map<String, Any>> {
        val request = restClient.get()
            .uri("$githubApiBase/repos/$owner/$repo/commits?sha=$branch&per_page=30")
            .header("Accept", "application/vnd.github+json")
        
        token?.let { request.header("Authorization", "Bearer $it") }
        
        @Suppress("UNCHECKED_CAST")
        return request.retrieve().body(List::class.java) as List<Map<String, Any>>
    }
}