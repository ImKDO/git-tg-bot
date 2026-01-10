package consumer

import boysband.linktracker.dto.kafka.ProviderConfigEvent
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import provider.ProviderRegistry

@Component
@Slf4j
class ConfigUpdateConsumer(
    private val logger: Logger,
    private val providerRegistry: ProviderRegistry
) {

    //TODO: дописать обработку
    @KafkaListener(
        topics = ["config-update"],
        groupId = "my-group",
    )
    fun consume(event: ProviderConfigEvent) {
        when (event.eventType) {
            ProviderConfigEvent.EventType.CREATED -> {
                providerRegistry.registerProvider(event.config)
                logger.warn("Create new provider.Provider with name: ${event.config.name}")
            }
            ProviderConfigEvent.EventType.UPDATED -> {
                logger.warn("Updating configuration for ProviderB: ${event.config.name}")
            }
            ProviderConfigEvent.EventType.DELETED -> {
                logger.warn("Received configuration update for unknown provider: ${event.config.name}")
            }
        }
    }
}
