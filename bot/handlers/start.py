from aiogram import Bot, Router, html
from aiogram.filters import CommandStart, Command
from aiogram.types import Message

router = Router(name="start")


@router.message(CommandStart())
async def cmd_start(msg: Message) -> None:
    """Обработчик команды /start"""
    await msg.answer(
        f"Привет, {html.bold(msg.from_user.full_name)}!\n\nЯ бот для работы с Git репозиториями."
    )


@router.message(Command("help"))
async def cmd_help(msg: Message, bot: Bot) -> None:
    """Обработчик команды /help"""
    commands = await bot.get_my_commands()
    help_text = "<b>Доступные команды:</b>\n\n"

    for cmd in commands:
        help_text += f"/{cmd.command} - {cmd.description}\n"

    await msg.answer(help_text)
