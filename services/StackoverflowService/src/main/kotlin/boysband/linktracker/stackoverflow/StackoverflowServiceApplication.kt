package boysband.linktracker.stackoverflow

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.time.ZonedDateTime


@SpringBootApplication
open class StackoverflowServiceApplication

fun main(args: Array<String>) {
    println(ZonedDateTime.now().minusYears(20))
    SpringApplicationBuilder(StackoverflowServiceApplication::class.java)
        .web(WebApplicationType.NONE)
        .run(*args)
}