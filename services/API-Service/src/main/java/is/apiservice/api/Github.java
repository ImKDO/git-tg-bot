package is.apiservice.api;


import is.apiservice.dto.enums.Issue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Builder
@Data
@Slf4j
public class Github {
    private RestTemplate restTemplate;

    String token;
    private Map<String, String> comments;
    private Map<String, String> users;
    private int countComments;
    private int countUsers;
    private List<String> labels;
    private String title;
    private String userOpened;
    private List<String> type;
    private List<String> tags;
    private Issue status;


    public void listenIssue(String linkIssue, String token){
        log.info("Прослушиваем issue: {}", linkIssue);
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        this.restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/vnd.github.v3+json");
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = this.restTemplate.exchange(
                linkIssue,
                HttpMethod.GET,
                request,
                String.class
        );

        log.info("Получили вот такую инфу:\n{}", response.getBody());

    }

}
