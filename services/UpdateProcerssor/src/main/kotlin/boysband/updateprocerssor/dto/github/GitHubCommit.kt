package boysband.updateprocerssor.dto.github

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class GitHubCommit(
    @JsonProperty("sha")
    val sha: String,
    
    @JsonProperty("html_url")
    val htmlUrl: String?,
    
    @JsonProperty("commit")
    val commit: CommitDetails,
    
    @JsonProperty("author")
    val author: GitHubUser?,
    
    @JsonProperty("committer")
    val committer: GitHubUser?
)

data class CommitDetails(
    @JsonProperty("message")
    val message: String,
    
    @JsonProperty("author")
    val author: CommitAuthor?,
    
    @JsonProperty("committer")
    val committer: CommitAuthor?
)

data class CommitAuthor(
    @JsonProperty("name")
    val name: String,
    
    @JsonProperty("email")
    val email: String,
    
    @JsonProperty("date")
    val date: Instant
)
