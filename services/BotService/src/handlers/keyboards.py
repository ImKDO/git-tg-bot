from aiogram.filters import callback_data
from aiogram.types import InlineKeyboardButton
from aiogram.utils.keyboard import InlineKeyboardBuilder


async def variants_auth():
  keyboard = InlineKeyboardBuilder()
  
  variants = {
    "С токеном": "token",
    "Без токена": "wtoken"
    }
  
  for k, v in variants.items():
    keyboard.add(InlineKeyboardButton(
      text=k,
      callback_data=v
      )
    )
  return keyboard.adjust(2).as_markup()

async def tracking():
  keyboard = InlineKeyboardBuilder()
