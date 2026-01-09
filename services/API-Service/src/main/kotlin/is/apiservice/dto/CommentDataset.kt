package `is`.apiservice.dto

/**
 * Датасет комментариев из GitHub Issue
 * @param numberIssue номер issue
 * @param numberCommentInIssue порядковый номер комментария в issue
 * @param authorComment автор комментария
 * @param commentText текст комментария
 */
data class CommentDataset(
    val numberIssue: Long,
    val numberCommentInIssue: Int,
    val authorComment: String,
    val commentText: String
)

