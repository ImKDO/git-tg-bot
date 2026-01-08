package is.apiservice.broker;

import is.apiservice.api.Github;
import is.apiservice.dto.TaskUserToken;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerService {
    private static Github github = null;

    public ConsumerService(
    ) {
        this.github = Github.builder().build();
    }

    @KafkaListener(topics = "task_user_token", groupId = "api-service")
    public void listenTokenUser(TaskUserToken taskUserToken) {
        try {
            log.info("Прослушиваем task_user_token");
            log.info("\nПолучены данные: \n{}", taskUserToken);

            String chatId = taskUserToken.getChatId();
            String token = taskUserToken.getToken();
            String linkIssue = taskUserToken.getLinkIssue();

            github.setToken(token);
            github.listenIssue(linkIssue, token);

        } catch (Exception e){
            log.error("Ошибочка получилась:\n{}", e.getMessage());
        }

    }
}
