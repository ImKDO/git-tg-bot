package `is`.apiservice.dto

import lombok.Data

@Data
class TaskUserToken {
    private val chatId: String? = null
    val token: String? = null
    val linkIssue: String? = null
}
