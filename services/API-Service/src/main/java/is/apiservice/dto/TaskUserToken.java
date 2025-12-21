package is.apiservice.dto;

import lombok.Data;

@Data
public class TaskUserToken {
    private String chatId;
    private String token;
    private String linkIssue;
}
