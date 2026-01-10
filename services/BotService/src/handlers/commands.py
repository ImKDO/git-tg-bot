import json
from collections import defaultdict
from aiogram import F, Router
from aiogram.filters import Command, StateFilter
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import State, StatesGroup
from aiogram.types import CallbackQuery, Message
from aiokafka import AIOKafkaProducer
from src.utils import keyboards as kb


routerConfigTracking = Router(name="config_tracking")


class ServiceConfig(StatesGroup):
    action_menu = State()
    waiting_choice = State()
    menu_loop = State()
    register_token = State()
    confirm_delete = State()


_registered_services: dict[int, set[str]] = defaultdict(set)
SERVICE_CODES = tuple(kb.SERVICE_VARIANTS.values())


def _service_title(code: str) -> str:
    for title, service_code in kb.SERVICE_VARIANTS.items():
        if service_code == code:
            return title
    return code


def _is_service_registered(chat_id: int, code: str) -> bool:
    return code in _registered_services.get(chat_id, set())


def _remember_service(chat_id: int, code: str) -> None:
    _registered_services[chat_id].add(code)


def _forget_service(chat_id: int, code: str) -> None:
    if chat_id in _registered_services:
        _registered_services[chat_id].discard(code)


async def _send_service_menu(
    message: Message,
    state: FSMContext,
    text: str | None = None,
    loop: bool = False,
) -> None:
    target_state = ServiceConfig.menu_loop if loop else ServiceConfig.waiting_choice
    await state.set_state(target_state)
    await message.answer(
        text or "Выберите доступные сервисы",
        reply_markup=await kb.variants_services(),
    )


async def _edit_service_menu(
    callback: CallbackQuery,
    state: FSMContext,
    text: str | None = None,
    loop: bool = False,
) -> None:
    target_state = ServiceConfig.menu_loop if loop else ServiceConfig.waiting_choice
    await state.set_state(target_state)
    await callback.message.edit_text(
        text or "Выберите доступные сервисы",
        reply_markup=await kb.variants_services(),
    )
    await callback.answer()


@routerConfigTracking.message(Command("config_tracking"))
async def config_tracking(msg: Message, state: FSMContext) -> None:
    await state.set_state(ServiceConfig.action_menu)
    await msg.answer(
        "Что вы хотите сделать?",
        reply_markup=await kb.main_actions_keyboard(),
    )


@routerConfigTracking.callback_query(
    ServiceConfig.action_menu,
    F.data == "action_history",
)
async def config_history(callback: CallbackQuery, state: FSMContext) -> None:
    await callback.answer("Раздел в разработке", show_alert=True)


@routerConfigTracking.callback_query(
    ServiceConfig.action_menu,
    F.data == "action_get",
)
async def config_get_notifications(callback: CallbackQuery, state: FSMContext) -> None:
    await callback.answer("Раздел в разработке", show_alert=True)


@routerConfigTracking.callback_query(
    ServiceConfig.action_menu,
    F.data == "action_config",
)
async def config_manage_services(callback: CallbackQuery, state: FSMContext) -> None:
    await callback.answer()
    await _edit_service_menu(callback, state)


@routerConfigTracking.callback_query(
    StateFilter(ServiceConfig.waiting_choice, ServiceConfig.menu_loop),
    F.data.in_(SERVICE_CODES),
)
async def service_chosen(callback: CallbackQuery, state: FSMContext) -> None:
    service_code = callback.data
    service_name = _service_title(service_code)
    chat_id = callback.message.chat.id
    await state.update_data(service_code=service_code, service_name=service_name)

    if _is_service_registered(chat_id, service_code):
        await state.set_state(ServiceConfig.confirm_delete)
        await callback.message.edit_text(
            f"Сервис {service_name} уже подключен. Удалить подписку?",
            reply_markup=await kb.confirm_remove_keyboard(),
        )
    else:
        await state.set_state(ServiceConfig.register_token)
        await callback.message.edit_text(
            f"Сервис {service_name} ещё не подключен. Введите токен для регистрации:",
            reply_markup=await kb.cancel_service_keyboard(),
        )

    await callback.answer()


@routerConfigTracking.callback_query(F.data == "back_to_services")
async def back_to_services(callback: CallbackQuery, state: FSMContext) -> None:
    await state.clear()
    await _edit_service_menu(callback, state, loop=True)


@routerConfigTracking.callback_query(F.data == "back_to_actions")
async def back_to_actions(callback: CallbackQuery, state: FSMContext) -> None:
    await state.clear()
    await state.set_state(ServiceConfig.action_menu)
    await callback.message.edit_text(
        "Что вы хотите сделать?",
        reply_markup=await kb.main_actions_keyboard(),
    )
    await callback.answer()


@routerConfigTracking.callback_query(F.data == "back_to_prev_block")
async def back_to_prev_block(callback: CallbackQuery, state: FSMContext) -> None:
    await state.clear()
    await state.set_state(ServiceConfig.action_menu)
    await callback.message.edit_text(
        "Вы вернулись в меню действий",
        reply_markup=await kb.main_actions_keyboard(),
    )
    await callback.answer()


@routerConfigTracking.message(ServiceConfig.register_token, F.text)
async def register_service(
    msg: Message,
    state: FSMContext,
    producer: AIOKafkaProducer,
) -> None:
    token_value = msg.text.strip()
    data = await state.get_data()
    service_code = data.get("service_code")
    service_name = data.get("service_name", service_code)

    if not service_code:
        await _send_service_menu(msg, state)
        return

    task = {
        "chat_id": msg.chat.id,
        "service": service_code,
        "token": token_value,
        "action": "register",
    }

    payload = json.dumps(task).encode("utf-8")
    await producer.send_and_wait("task_user_service", payload)
    _remember_service(msg.chat.id, service_code)

    await msg.answer(f"Сервис {service_name} успешно зарегистрирован.")
    await state.clear()
    await _send_service_menu(msg, state, loop=True)


@routerConfigTracking.callback_query(
    ServiceConfig.confirm_delete,
    F.data == "delete_service",
)
async def delete_service(
    callback: CallbackQuery,
    state: FSMContext,
    producer: AIOKafkaProducer,
) -> None:
    data = await state.get_data()
    service_code = data.get("service_code")
    service_name = data.get("service_name", service_code)

    if not service_code:
        await _edit_service_menu(callback, state)
        return

    task = {
        "chat_id": callback.message.chat.id,
        "service": service_code,
        "action": "delete",
    }

    payload = json.dumps(task).encode("utf-8")
    await producer.send_and_wait("task_user_service", payload)
    _forget_service(callback.message.chat.id, service_code)

    await callback.message.edit_text(f"Сервис {service_name} удалён.")
    await callback.answer()
    await state.clear()
    await _send_service_menu(callback.message, state, loop=True)
