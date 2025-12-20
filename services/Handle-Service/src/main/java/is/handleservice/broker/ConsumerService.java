package is.handleservice.broker;

import is.handleservice.handle.GithubHandle;
import is.handleservice.dto.TaskUserToken;
import is.handleservice.webhook.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.stereotype.Service;
import org.springframework.util.backoff.FixedBackOff;

import java.io.IOException;

@Service
public class ConsumerService {

    private static final Logger log = LoggerFactory.getLogger(
        ConsumerService.class
    );
    private final GithubHandle githubHandle;
    private final Issue issue;
    public ConsumerService(
            GithubHandle githubHandle,
            Issue issue
    ) {
        this.githubHandle = githubHandle;
        this.issue = issue;
    }

    @KafkaListener(topics = "task_user_token", groupId = "auth-service")
    public void listenUserTokenTasks(TaskUserToken task) throws IOException {
        try{
            log.info("\nПолучены данные: {}\n", task);

            String chatId = task.getChatId();
            String token = task.getToken();

            githubHandle.handlerGithub(task);
            issue.handlerGetIssue(chatId, token);
            log.info("\n---\nЮзер: {} \nТокен:{}\n---\n", chatId, token);

        } catch (Exception ex ){
            log.error("Ошибка: {}", ex.getMessage());
        }
    }
    @Bean
    public CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(0L, 0));
        handler.addNotRetryableExceptions(DeserializationException.class);
        return handler;
    }
}
