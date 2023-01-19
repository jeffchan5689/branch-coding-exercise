package com.branch.exercise.gateway;

import com.branch.exercise.gateway.dto.GithubRepoDto;
import com.branch.exercise.gateway.dto.GithubUserDto;
import com.branch.exercise.service.NoGithubUserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Service
public class GithubGateway {

    private final String GITHUB_BASE_URL = "https://api.github.com";

    private final WebClient client = WebClient.builder()
            .baseUrl(GITHUB_BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public GithubUserDto getGithubUserForUsername(String username) {
        String uri = "/users/" + username;

        GithubUserDto dtoResponse = client.get()
                .uri(uri)
                .exchangeToMono(response ->
                    response.bodyToMono(GithubUserDto.class)
                )
                .block(Duration.ofSeconds(5));

        if (dtoResponse == null || dtoResponse.login == null) {
            throw new NoGithubUserException(username);
        }

        return dtoResponse;
    }

    public List<GithubRepoDto> getGithubReposForUser(String username) {
        String uri = "/users/" + username + "/repos";

        GithubRepoDto[] repoDtoArray = client.get()
                .uri(uri)
                .exchangeToMono(response ->
                        response.bodyToMono(GithubRepoDto[].class)
                )
                .block(Duration.ofSeconds(5));

        return Arrays.stream(repoDtoArray != null ? repoDtoArray : new GithubRepoDto[0]).toList();
    }

}
