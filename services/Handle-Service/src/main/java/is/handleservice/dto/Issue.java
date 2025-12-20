package is.handleservice.dto;

import is.handleservice.dto.enums.IssueStatusEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Issue {
    private Map<String, String> comments;
    private Map<String, String> users;
    private int countComments;
    private int countUsers;
    private List<String> labels;
    private String title;
    private String userOpened;
    private List<String> type;
    private List<String> tags;
    private IssueStatusEnum status;

}
