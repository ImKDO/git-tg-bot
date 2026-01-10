package boysband.linktracker.core.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class HistoryAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var chatId: Int,
    @Column(nullable = false, length = 1000)
    var response: String,
    @Column(nullable = false)
    var date: LocalDateTime,
) {
}