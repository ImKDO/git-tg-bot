package `is`.apiservice.dto
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonGithubUser (
    val login: String,
    val id: Long
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class JsonGithubIssueComments (
    val id: Long,
    val body: String,
    val user: JsonGithubUser,
)

