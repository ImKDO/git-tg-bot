package boysband.updateprocerssor.dto.github

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class GitHubWorkflowRuns(
    @JsonProperty("total_count")
    val totalCount: Int,
    
    @JsonProperty("workflow_runs")
    val workflowRuns: List<GitHubWorkflowRun>
)

data class GitHubWorkflowRun(
    @JsonProperty("id")
    val id: Long,
    
    @JsonProperty("name")
    val name: String?,
    
    @JsonProperty("head_branch")
    val headBranch: String?,
    
    @JsonProperty("head_sha")
    val headSha: String?,
    
    @JsonProperty("status")
    val status: String,
    
    @JsonProperty("conclusion")
    val conclusion: String?,
    
    @JsonProperty("html_url")
    val htmlUrl: String,
    
    @JsonProperty("created_at")
    val createdAt: Instant,
    
    @JsonProperty("updated_at")
    val updatedAt: Instant,
    
    @JsonProperty("run_number")
    val runNumber: Int,
    
    @JsonProperty("event")
    val event: String,
    
    @JsonProperty("actor")
    val actor: GitHubUser?
)
