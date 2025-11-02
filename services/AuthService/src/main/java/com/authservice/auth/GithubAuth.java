package com.authservice.auth;

import com.authservice.dto.TaskUserToken;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class GithubAuth {

    private Logger logger = LoggerFactory.getLogger(GithubAuth.class);

    public void handlerGithub(TaskUserToken taskUserToken) throws IOException {

            String token = taskUserToken.getToken();

            try{
                GitHub github = new GitHubBuilder().withOAuthToken(token).build();
                logger.info("Привет, эт {}", github.getRepository("ImKDO/git-tg-bot").listCommits().toList().get(0).getSHA1());
            }
            catch (IOException e){
                logger.error(e.getMessage());
            }
    }
}
