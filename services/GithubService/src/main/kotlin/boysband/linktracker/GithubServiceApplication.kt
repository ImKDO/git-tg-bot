package boysband.linktracker

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class GithubServiceApplication

fun main(args: Array<String>) {
	SpringApplicationBuilder(GithubServiceApplication::class.java)
		.web(WebApplicationType.NONE)
		.run(*args)
}
