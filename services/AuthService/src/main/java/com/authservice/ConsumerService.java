package com.authservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics= "task_user_token", groupId="auth-service")
    public void listenUserTokenTasks(TaskUserToken task){
        log.info("\nПолучены данные: {}\n", task);

        String chatId = task.getChatId();
        String token = task.getToken();

        log.info("\n---\nЮзер: {} \nТокен:{}\n---\n", chatId, token);
    }
}
