package boysband.linktracker.stackoverflow.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class LoggerConfig {

    @Bean
    open fun logger(): Logger = LoggerFactory.getLogger("app-logger")
}