package com.authservice.webhook;

import com.authservice.dto.TaskUserToken;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Issue {
    private Logger logger = LoggerFactory.getLogger(Issue.class);

    public void handlerGetIssue (String token, String issueLink) throws IOException {
        try{
            GitHub github = new GitHubBuilder().withOAuthToken(token).build();
            logger.info("Auth user {} passed", token);
            var issues = github.searchIssues();
            logger.info("Я получил данные вот такого объекта: {}", issues.list().getTotalCount());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
}