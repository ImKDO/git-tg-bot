package is.apiservice.dto;

import lombok.Data;

@Data
public class UserToken {
    private String chatId;
    private String token;
    private String linkIssue;
}
