from aiogram.filters import callback_data
from aiogram.types import InlineKeyboardButton
from aiogram.utils.keyboard import InlineKeyboardBuilder

SERVICE_VARIANTS = {
    "GitHub": "GH",
    "StackOverflow": "SO",
}

MAIN_ACTION_BUTTONS = (
    ("История уведомлений", "action_history"),
    ("Настройка получения уведомления", "action_config"),
    ("Получить уведомление", "action_get"),
)


async def main_actions_keyboard():
    keyboard = InlineKeyboardBuilder()
    for title, callback in MAIN_ACTION_BUTTONS:
        keyboard.add(InlineKeyboardButton(text=title, callback_data=callback))
    return keyboard.adjust(1).as_markup()


async def variants_auth():
    keyboard = InlineKeyboardBuilder()

    variants = {
        "С токеном": "token",
        "Без токена": "wtoken",
    }

    for k, v in variants.items():
        keyboard.add(
            InlineKeyboardButton(
                text=k,
                callback_data=v,
            )
        )
    return keyboard.adjust(2).as_markup()


async def variants_services(variants: dict | None = None):
    keyboard = InlineKeyboardBuilder()
    services = variants or SERVICE_VARIANTS
    for title, code in services.items():
        keyboard.add(
            InlineKeyboardButton(
                text=title,
                callback_data=code,
            )
        )
    keyboard.add(
        InlineKeyboardButton(
            text="Назад", callback_data="back_to_prev_block"
        )
    )
    keyboard.add(
        InlineKeyboardButton(
            text="Главное меню", callback_data="back_to_actions"
        )
    )
    return keyboard.adjust(2).as_markup()


async def confirm_remove_keyboard():
    keyboard = InlineKeyboardBuilder()
    keyboard.add(
        InlineKeyboardButton(
            text="Удалить", callback_data="delete_service"
        )
    )
    keyboard.add(
        InlineKeyboardButton(
            text="Назад к сервисам", callback_data="back_to_services"
        )
    )
    keyboard.add(
        InlineKeyboardButton(
            text="Главное меню", callback_data="back_to_actions"
        )
    )
    return keyboard.adjust(1).as_markup()


async def cancel_service_keyboard():
    keyboard = InlineKeyboardBuilder()
    keyboard.add(
        InlineKeyboardButton(
            text="Назад к сервисам", callback_data="back_to_services"
        )
    )
    keyboard.add(
        InlineKeyboardButton(
            text="Главное меню", callback_data="back_to_actions"
        )
    )
    return keyboard.adjust(1).as_markup()
