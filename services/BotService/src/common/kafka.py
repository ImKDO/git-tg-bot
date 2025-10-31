from aiokafka import AIOKafkaProducer
from src.config import settings

async def get_producer() -> AIOKafkaProducer:
  producer = AIOKafkaProducer(
  bootstrap_servers=settings.KAFKA_BOOTSTRAP_SERVERS
  )
  print("---------------------- Kafka Producer Created ---------------------)")
  return producer