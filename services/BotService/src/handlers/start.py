from aiogram import Bot, Router, html
from aiogram.filters import CommandStart, Command
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import State, StatesGroup
from aiogram.types import Message


router = Router(name="start")


@router.message(CommandStart())
async def cmd_start(msg: Message) -> None:
  await msg.answer(
      f"Привет, {html.bold(msg.from_user.full_name)}!\n\nЯ бот для работы с Git репозиториями."
  )

@router.message(Command("help"))
async def cmd_help(msg: Message, src: Bot) -> None:
  commands = await src.get_my_commands()
  help_text = "<b>Доступные команды:</b>\n\n"

  for cmd in commands:
      help_text += f"/{cmd.command} - {cmd.description}\n"

  await msg.answer(help_text)

