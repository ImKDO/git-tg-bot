package boysband.updateprocerssor

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UpdateProcerssorApplication

fun main(args: Array<String>) {
	runApplication<UpdateProcerssorApplication>(*args) {
		setWebApplicationType(WebApplicationType.NONE)
	}
}
