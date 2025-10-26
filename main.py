import asyncio
import logging
import sys
import token
from aiogram import Bot, Dispatcher, html
from aiogram.client.default import DefaultBotProperties
from aiogram.enums import ParseMode
from aiogram.filters import CommandStart
from dotenv import dotenv_values
from aiogram.types import Message

cfg = dotenv_values(".env")
TOKEN = cfg['BOT_TOKEN']

dp = Dispatcher()

@dp.message(CommandStart())
async def command_start_handler(msg: Message) -> None:
  await msg.answer(f'Hello, {html.bold(msg.from_user.full_name)}!')

@dp.message()
async def handler(msg: Message) -> None:
  try:
    await msg.send_copy(chat_id=msg.chat.id)
  except TypeError:
    await msg.answer("Nice try!")


async def main() -> None:
  if TOKEN is None:
    print("Token is None")
    return
  bot = Bot(token=TOKEN, default=DefaultBotProperties(parse_mode=ParseMode.HTML))
  await dp.start_polling(bot)



if __name__ == "__main__":
  logging.basicConfig(level=logging.INFO, stream=sys.stdout)
  asyncio.run(main())