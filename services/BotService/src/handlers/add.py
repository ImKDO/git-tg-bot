
import json
from aiogram import F, Router
from aiogram.filters import Command
from aiogram.fsm.context import FSMContext
from aiogram.fsm.state import State, StatesGroup
from aiogram.types import CallbackQuery, Message
from aiokafka import AIOKafkaProducer
import src.handlers.keyboards as kb


router = Router(name="add")

class AddAuth(StatesGroup):
  waiting_choice = State()
  
  with_token = State()
  without_token = State()

@router.message(Command("add"))
async def cmd_add(msg: Message, state: FSMContext)-> None:
  await msg.answer(text="Выберите способ аутентификации", reply_markup=await kb.variants_auth())
  await state.set_state(AddAuth.waiting_choice)


@router.callback_query(AddAuth.waiting_choice, F.data == "token")
async def with_token(callback: CallbackQuery, state: FSMContext) -> None:
  
  await state.set_state(AddAuth.with_token)
  
  await callback.message.edit_text("Введите плез токен")
  
  await callback.answer()
  
@router.message(AddAuth.with_token, F.text)
async def process_with_token(
  msg: Message, 
  state: FSMContext,
  producer: AIOKafkaProducer
  ) -> None:
  
  text = msg.text
  
  #... implementation add do db
  
  task = {
    "chat_id": "{}".format(msg.chat.id),
    "token": "{}".format(text),
  }
  
  task_json = json.dumps(task).encode("utf-8")
  
  await producer.send_and_wait("task_user_token" ,task_json)
  
  await state.update_data(token=text)
  
  await msg.answer("Токен '{}' получили".format(text))
  
  await state.clear() 