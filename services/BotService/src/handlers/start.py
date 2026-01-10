from aiogram import Bot, Router, html
from aiogram.filters import Command, CommandStart
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import State, StatesGroup
from aiogram.types import Message
from src.utils import keyboards as kb
from .commands import ServiceConfig

router = Router(name="start")


@router.message(CommandStart())
async def cmd_start(msg: Message, state: FSMContext) -> None:
    await msg.answer(
        f"Привет, {html.bold(msg.from_user.full_name)}!\n\nЯ бот для работы с получения нотификаций с разных сервисов!"
    )
    await state.set_state(ServiceConfig.action_menu)
    await msg.answer(
        "Что вы хотите сделать?",
        reply_markup=await kb.main_actions_keyboard(),
    )


@router.message(Command("help"))
async def cmd_help(msg: Message, bot: Bot) -> None:
    """Отправляет пользователю список доступных команд."""
    commands = await bot.get_my_commands()
    help_text = "<b>Доступные команды:</b>\n\n"

    for cmd in commands:
        help_text += f"/{cmd.command} - {cmd.description}\n"

    await msg.answer(help_text)
