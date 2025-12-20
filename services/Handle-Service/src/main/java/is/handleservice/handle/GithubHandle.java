package is.handleservice.handle;

import is.handleservice.dto.TaskUserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
public class GithubHandle {

    private final Logger logger = LoggerFactory.getLogger(GithubHandle.class);
    private final RestTemplate restTemplate;

    public GithubHandle(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void handlerGithub(TaskUserToken taskUserToken) throws IOException {
        String token = taskUserToken.getToken();

        String url = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/vnd.github.v3+json");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            logger.info("Неккоректный статус доступа: {}", HttpStatus.OK);
        }

        logger.info("Получили: {}", response.getStatusCode());
    }
}
