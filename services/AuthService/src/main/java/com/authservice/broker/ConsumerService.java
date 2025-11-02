package com.authservice.broker;

import com.authservice.auth.GithubAuth;
import com.authservice.dto.TaskUserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);
    private GithubAuth githubAuth = new GithubAuth();

    @Autowired
    public ConsumerService(GithubAuth githubAuth) {
        this.githubAuth = githubAuth;
    }

    @KafkaListener(topics= "task_user_token", groupId="auth-service")
    public void listenUserTokenTasks(TaskUserToken task) throws IOException {
        log.info("\nПолучены данные: {}\n", task);

        String chatId = task.getChatId();
        String token = task.getToken();

        githubAuth.handlerGithub(task);

        log.info("\n---\nЮзер: {} \nТокен:{}\n---\n", chatId, token);
    }
}
