from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    BOT_TOKEN: str

    DATABASE_URL: str = ""
    REDIS_URL: str = ""
    KAFKA_BOOTSTRAP_SERVERS: str

    model_config = SettingsConfigDict(
        env_file=".env", case_sensitive=False, extra="ignore"
    )


settings = Settings()
