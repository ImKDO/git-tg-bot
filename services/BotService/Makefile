.PHONY: help install dev check format lint type-check run clean test

help:
	@echo "Git Telegram src - Makefile команды"
	@echo ""
	@echo "Использование:"
	@echo "  make install      - Установить зависимости"
	@echo "  make dev          - Установить зависимости для разработки"
	@echo "  make check        - Проверить конфигурацию"
	@echo "  make format       - Отформатировать код"
	@echo "  make lint         - Проверить код линтером"
	@echo "  make type-check   - Проверить типы"
	@echo "  make run          - Запустить бота"
	@echo "  make clean        - Очистить кэш и временные файлы"
	@echo "  make test         - Запустить тесты"
	@echo ""

install:
	@echo "Установка зависимостей..."
	uv sync

dev:
	@echo "Установка зависимостей для разработки..."
	uv sync --group dev

check:
	@echo "Проверка конфигурации..."
	uv run python check_config.py

format:
	@echo "Форматирование кода..."
	uv run ruff format .

lint:
	@echo "Проверка кода линтером..."
	uv run ruff check .
	@echo "Автоматическое исправление проблем..."
	uv run ruff check --fix .

type-check:
	@echo "Проверка типов..."
	uv run mypy src/

run: check
	@echo "Запуск бота..."
	uv run python -m src

clean:
	@echo "Очистка кэша и временных файлов..."
	find . -type d -name "__pycache__" -exec rm -rf {} + 2>/dev/null || true
	find . -type f -name "*.pyc" -delete
	find . -type f -name "*.pyo" -delete
	find . -type f -name "*.py,cover" -delete
	find . -type d -name "*.egg-info" -exec rm -rf {} + 2>/dev/null || true
	find . -type d -name ".pytest_cache" -exec rm -rf {} + 2>/dev/null || true
	find . -type d -name ".mypy_cache" -exec rm -rf {} + 2>/dev/null || true
	find . -type d -name ".ruff_cache" -exec rm -rf {} + 2>/dev/null || true
	@echo "Очистка завершена!"

test:
	@echo "Запуск тестов..."
	@echo "Тесты пока не реализованы"

all: dev format lint type-check
	@echo "Все проверки выполнены!"
