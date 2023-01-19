package com.branch.exercise.gateway;

import com.branch.exercise.gateway.dto.GithubRepoDto;
import com.branch.exercise.gateway.dto.GithubUserDto;
import com.branch.exercise.service.NoGithubUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class GithubGateway {

    @Autowired
    private RestTemplate restTemplate;

    private final String GITHUB_BASE_URL = "https://api.github.com";

    Logger logger = LoggerFactory.getLogger(GithubGateway.class);

    public GithubUserDto getGithubUserForUsername(String username) {
        logger.info("Reaching out to Github for user data for username: " + username);

        String uri = "/users/" + username;

        GithubUserDto dtoResponse = restTemplate.getForObject(GITHUB_BASE_URL + uri, GithubUserDto.class);

        if (dtoResponse == null || dtoResponse.login == null) {
            throw new NoGithubUserException(username);
        }

        return dtoResponse;
    }

    public List<GithubRepoDto> getGithubReposForUser(String username) {
        logger.info("Reaching out to Github for user repo data for username: " + username);

        String uri = "/users/" + username + "/repos";

        GithubRepoDto[] repoDtoArray = restTemplate.getForObject(GITHUB_BASE_URL + uri, GithubRepoDto[].class);

        return Arrays.stream(repoDtoArray != null ? repoDtoArray : new GithubRepoDto[0]).toList();
    }
}
