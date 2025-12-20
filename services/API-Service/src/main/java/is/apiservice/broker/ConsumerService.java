package is.apiservice.broker;

import is.apiservice.api.Github;
import is.apiservice.dto.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);
    private static Github github = null;

    public ConsumerService(
    ) {
        this.github = Github.builder().build();
    }

    @KafkaListener(topics = "task_user_token", groupId = "api-service")
    public void listenTokenUser(UserToken userToken) {
        try {
            log.info("Прослушиваем task_user_token");
            log.info("\nПолучены данные: \n{}", userToken);

            String chatId = userToken.getChatId();
            String token = userToken.getToken();
            String linkIssue = userToken.getLinkIssue();

            github.setToken(token);
            github.listenIssue(linkIssue);


        } catch (Exception e){
            log.error("Ошибочка получилась:\n{}", e.getMessage());
        }

    }
}
