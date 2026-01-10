package boysband.linktracker.boysband.linktracker.stackoverflow

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZonedDateTime


@SpringBootApplication
open class StackoverflowServiceApplication

fun main(args: Array<String>) {
    println(ZonedDateTime.now().minusYears(20))
    runApplication<StackoverflowServiceApplication>(*args) {
        webApplicationType = WebApplicationType.NONE
    }
}