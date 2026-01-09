import asyncio
import logging
import sys

from aiogram import Bot, Dispatcher
from aiogram.client.default import DefaultBotProperties
from aiogram.enums import ParseMode
from aiogram.types import BotCommand

from src.common.kafka import get_producer
from src.config import settings
from src.handlers import start, add


logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    stream=sys.stdout,
)
logger = logging.getLogger(__name__)

COMMANDS = [
    BotCommand(command="start", description="Начать работу с ботом"),
    BotCommand(command="help", description="Получить справку по командам"),
    BotCommand(command="add", description="Добавить новый сервис"),
    BotCommand(command="history", description="Просмотреть историю уведомлений"),
    BotCommand(command="config_notification", description="Настройка прослушивания уведомлений"),
    BotCommand(command="get_notification", description="Получить нотификации"),
    BotCommand(command="config_tracking", description="Отслеживания")
]

async def main() -> None:
    bot = Bot(
        token=settings.BOT_TOKEN,
        default=DefaultBotProperties(parse_mode=ParseMode.HTML),
    )

    dp = Dispatcher()

    producer = await get_producer()
    
    dp["producer"] = producer
    
    dp.startup.register(producer.start)
    dp.shutdown.register(producer.stop)

    dp.include_router(start.router)
    dp.include_router(add.router)

    await bot.set_my_commands(COMMANDS)

    logger.info("Бот запущен и готов к работе")

    try:
        await dp.start_polling(bot, allowed_updates=dp.resolve_used_update_types())
    finally:
        await bot.session.close()


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        logger.info("Бот остановлен")
