package is.handleservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskUserToken {
    private String chatId;
    private String token;
    private String linkIssue;
}
