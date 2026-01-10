package boysband.linktracker.core.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
class Action(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne() @JoinColumn(name = "method_id")
    var method: Method,

    @OneToOne() @JoinColumn(name = "token_id")
    var token: Token,

    @Column(nullable = false)
    var chatId: Int,

    @ManyToOne() @JoinColumn(name = "service_id")
    var service: Service,

    @Column(nullable = false, length = 16)
    var describe: String,

    var date: LocalDateTime
) {

}