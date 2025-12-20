package is.handleservice.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class Issue {
    private final Logger logger = LoggerFactory.getLogger(Issue.class);
    private final RestTemplate restTemplate;

    public Issue(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void handlerGetIssue (String token, String issueLink) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                issueLink,
                HttpMethod.GET,
                request,
                String.class
        );
        logger.info("Я получил данные вот такого объекта: {}", response.getBody());
    }
}