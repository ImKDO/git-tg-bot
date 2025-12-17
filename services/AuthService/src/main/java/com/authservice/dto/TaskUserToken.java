package com.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskUserToken {
    @JsonProperty("chat_id")
    private String chatId;
    private String token;
    private String linkIssue;
}
