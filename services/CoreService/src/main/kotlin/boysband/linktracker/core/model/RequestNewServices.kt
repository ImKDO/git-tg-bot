package boysband.linktracker.core.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.Getter
import java.time.LocalDateTime

@Entity
class RequestNewServices(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,

    @Column(nullable = false)
    var chatId: Int,

    @Column(nullable = false)
    var link: String,

    @Column(nullable = false)
    var date: LocalDateTime
) {
}