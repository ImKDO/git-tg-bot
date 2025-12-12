import logging

from aiokafka import AIOKafkaProducer
from aiokafka.errors import KafkaError

from src.config import settings

logger = logging.getLogger(__name__)


async def get_producer() -> AIOKafkaProducer:
    if not settings.KAFKA_BOOTSTRAP_SERVERS:
        raise ValueError("KAFKA_BOOTSTRAP_SERVERS is not configured")

    try:
        producer = AIOKafkaProducer(
            bootstrap_servers=settings.KAFKA_BOOTSTRAP_SERVERS,
            metadata_max_age_ms=30_000,
            request_timeout_ms=20_000,
        )
    except KafkaError as exc:
        raise RuntimeError("Failed to instantiate Kafka producer") from exc

    logger.info("Kafka producer configured for %s", settings.KAFKA_BOOTSTRAP_SERVERS)
    return producer
