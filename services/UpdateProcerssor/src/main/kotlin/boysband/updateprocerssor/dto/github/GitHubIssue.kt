package boysband.updateprocerssor.dto.github

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class GitHubIssue(
    @JsonProperty("id")
    val id: Long,
    
    @JsonProperty("number")
    val number: Int,
    
    @JsonProperty("title")
    val title: String,
    
    @JsonProperty("state")
    val state: String,
    
    @JsonProperty("body")
    val body: String?,
    
    @JsonProperty("html_url")
    val htmlUrl: String,
    
    @JsonProperty("user")
    val user: GitHubUser?,
    
    @JsonProperty("labels")
    val labels: List<GitHubLabel> = emptyList(),
    
    @JsonProperty("assignees")
    val assignees: List<GitHubUser> = emptyList(),
    
    @JsonProperty("created_at")
    val createdAt: Instant,
    
    @JsonProperty("updated_at")
    val updatedAt: Instant,
    
    @JsonProperty("closed_at")
    val closedAt: Instant?
)

data class GitHubUser(
    @JsonProperty("id")
    val id: Long,
    
    @JsonProperty("login")
    val login: String,
    
    @JsonProperty("avatar_url")
    val avatarUrl: String?,
    
    @JsonProperty("html_url")
    val htmlUrl: String?
)

data class GitHubLabel(
    @JsonProperty("id")
    val id: Long,
    
    @JsonProperty("name")
    val name: String,
    
    @JsonProperty("color")
    val color: String?
)
