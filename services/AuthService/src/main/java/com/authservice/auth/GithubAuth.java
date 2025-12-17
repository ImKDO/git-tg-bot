package com.authservice.auth;

import com.authservice.dto.TaskUserToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.stereotype.Service;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
public class GithubAuth {

    private Logger logger = LoggerFactory.getLogger(GithubAuth.class);
    private final RestTemplate restTemplate;

    public GithubAuth(RestTemplateBuilder restTemplateBuilder) {
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
        logger.info("Получили: {}", response.getStatusCode());
    }
}
