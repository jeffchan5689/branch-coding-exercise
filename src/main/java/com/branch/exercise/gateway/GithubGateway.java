package com.branch.exercise.gateway;

import com.branch.exercise.controller.dto.RepoDto;
import com.branch.exercise.controller.dto.VersionControlUserDto;
import com.branch.exercise.gateway.dto.GithubRepoDto;
import com.branch.exercise.gateway.dto.GithubUserDto;
import com.branch.exercise.service.GithubClientErrorException;
import com.branch.exercise.service.GithubServerErrorException;
import com.branch.exercise.service.NoGithubUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GithubGateway {

    @Autowired
    private RestTemplate restTemplate;

    private final String GITHUB_BASE_URL = "https://api.github.com";

    Logger logger = LoggerFactory.getLogger(GithubGateway.class);

    public VersionControlUserDto getGithubUserForUsername(String username) {
        logger.info("Reaching out to Github for user data for username: " + username);

        String uri = "/users/" + username;

        ResponseEntity<GithubUserDto> response = restTemplate.exchange(GITHUB_BASE_URL + uri, HttpMethod.GET, null, GithubUserDto.class);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new NoGithubUserException(username);
        } else if (response.getStatusCode().is4xxClientError()) {
            throw new GithubClientErrorException(response.getStatusCode().toString());
        } else if (response.getStatusCode().is5xxServerError()) {
            throw new GithubServerErrorException(response.getStatusCode().toString());
        }

        return mapGithubUserToVersionControlUser(response.getBody());
    }

    public List<RepoDto> getGithubReposForUser(String username) {
        logger.info("Reaching out to Github for user repo data for username: " + username);

        String uri = "/users/" + username + "/repos";

        GithubRepoDto[] repoDtoArray = restTemplate.getForObject(GITHUB_BASE_URL + uri, GithubRepoDto[].class);

        List<GithubRepoDto> repoList = Arrays.stream(repoDtoArray != null ? repoDtoArray : new GithubRepoDto[0]).toList();

        return repoList.stream()
                .map(this::mapGithubReposToVersionControlRepo)
                .collect(Collectors.toList());
    }

    private VersionControlUserDto mapGithubUserToVersionControlUser(GithubUserDto githubUserDto) {
        VersionControlUserDto versionControlUserDto = new VersionControlUserDto();

        versionControlUserDto.userName = githubUserDto.login;
        versionControlUserDto.displayName = githubUserDto.name;
        versionControlUserDto.avatar = githubUserDto.avatarUrl;
        versionControlUserDto.geoLocation = githubUserDto.geoLocation;
        versionControlUserDto.email = githubUserDto.email;
        versionControlUserDto.url = githubUserDto.url;
        versionControlUserDto.createdAt = githubUserDto.createdAt.toLocalDateTime();

        return versionControlUserDto;
    }

    private RepoDto mapGithubReposToVersionControlRepo(GithubRepoDto githubRepoDto) {
        RepoDto repoDto = new RepoDto();

        repoDto.name = githubRepoDto.name;
        repoDto.url = githubRepoDto.htmlUrl;

        return repoDto;
    }
}
