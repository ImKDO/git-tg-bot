package is.apiservice.api;


import is.apiservice.dto.enums.Issue;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class Github {
    private Logger log =  LoggerFactory.getLogger(this.getClass());
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

    public void listenIssue(String linkIssue){
        log.info("Прослушиваем issue: {}", linkIssue);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(linkIssue);
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
