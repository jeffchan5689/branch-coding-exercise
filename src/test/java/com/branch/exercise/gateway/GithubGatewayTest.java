package com.branch.exercise.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.branch.exercise.controller.dto.RepoDto;
import com.branch.exercise.controller.dto.VersionControlUserDto;
import com.branch.exercise.gateway.dto.GithubRepoDto;
import com.branch.exercise.gateway.dto.GithubUserDto;
import com.branch.exercise.service.GithubClientErrorException;
import com.branch.exercise.service.GithubServerErrorException;
import com.branch.exercise.service.NoGithubUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GithubGatewayTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubGateway githubGateway;

    private GithubUserDto githubUserDto;

    private GithubRepoDto[] githubRepoDtos;

    private final String GITHUB_BASE_URL = "https://api.github.com";

    @BeforeEach()
    public void setUp() {
        setDefaultObjects();
    }

    @Test
    public void gettingGithubUserReturnsRetrievedAndCorrectlyMappedData() {
        String username = "octocat";

        String fullUrl = GITHUB_BASE_URL + "/users/" + username;
        when(restTemplate.exchange(fullUrl, HttpMethod.GET, null, GithubUserDto.class)).thenReturn(new ResponseEntity<>(githubUserDto, HttpStatus.OK));

        VersionControlUserDto githubUserDto = githubGateway.getGithubUserForUsername(username);

        assertNotNull(githubUserDto);
        assertNotNull(githubUserDto.userName);
        assertNotNull(githubUserDto.displayName);
    }

    @Test
    public void gettingGithubUserThrowsAnExceptionIfNoLoginIsPresent() {
        String username = "octocat";

        String fullUrl = GITHUB_BASE_URL + "/users/" + username;
        when(restTemplate.exchange(fullUrl, HttpMethod.GET, null, GithubUserDto.class)).thenReturn(new ResponseEntity<>(githubUserDto, HttpStatus.NOT_FOUND));

        NoGithubUserException exception = assertThrows(
                NoGithubUserException.class,
                () -> githubGateway.getGithubUserForUsername(username));

        assertEquals("No Github user found for username: octocat", exception.getMessage());
    }

    @Test
    public void gettingGithubUserThrowsAnExceptionIfGithubReturnsAServerError() {
        String username = "octocat";

        String fullUrl = GITHUB_BASE_URL + "/users/" + username;
        when(restTemplate.exchange(fullUrl, HttpMethod.GET, null, GithubUserDto.class)).thenReturn(new ResponseEntity<>(githubUserDto, HttpStatus.INTERNAL_SERVER_ERROR));

        GithubServerErrorException exception = assertThrows(
                GithubServerErrorException.class,
                () -> githubGateway.getGithubUserForUsername(username));

        assertEquals("Error communicating with Github: 500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }

    @Test
    public void gettingGithubUserThrowsAnExceptionIfGithubReturnsAClientError() {
        String username = "octocat";
        GithubUserDto serverResponse = new GithubUserDto();

        String fullUrl = GITHUB_BASE_URL + "/users/" + username;
        when(restTemplate.exchange(fullUrl, HttpMethod.GET, null, GithubUserDto.class)).thenReturn(new ResponseEntity<>(githubUserDto, HttpStatus.BAD_REQUEST));

        GithubClientErrorException exception = assertThrows(
                GithubClientErrorException.class,
                () -> githubGateway.getGithubUserForUsername(username));

        assertEquals("Bad request communicating with Github: 400 BAD_REQUEST", exception.getMessage());
    }

    @Test
    public void gettingGithubUserReposReturnsRetrievedData() {
        String username = "octocat";

        String fullUrl = GITHUB_BASE_URL + "/users/" + username + "/repos";
        when(restTemplate.getForObject(fullUrl, GithubRepoDto[].class)).thenReturn(githubRepoDtos);

        List<RepoDto> repoDtoList = githubGateway.getGithubReposForUser(username);

        assertEquals("repo1", repoDtoList.get(0).name);
        assertEquals("https://github.com/octocat/repo-1", repoDtoList.get(0).url);
        assertEquals("repo2", repoDtoList.get(1).name);
        assertEquals("https://github.com/octocat/repo-2", repoDtoList.get(1).url);
    }

    private void setDefaultObjects() {
        githubUserDto = new GithubUserDto();
        githubUserDto.login = "octocat";
        githubUserDto.name = "The Octocat";
        githubUserDto.avatarUrl = "https://avatars3.githubusercontent.com/u/583231?v=4";
        githubUserDto.geoLocation = "San Francisco";
        githubUserDto.email = "octocat@github.com";
        githubUserDto.url = "https://github.com/octocat";
        githubUserDto.createdAt = OffsetDateTime.of(LocalDateTime.of(2011, 1, 25, 18, 44, 36), ZoneOffset.UTC);;

        GithubRepoDto githubRepoDto1 = new GithubRepoDto();
        githubRepoDto1.name = "repo1";
        githubRepoDto1.htmlUrl = "https://github.com/octocat/repo-1";

        GithubRepoDto githubRepoDto2 = new GithubRepoDto();
        githubRepoDto2.name = "repo2";
        githubRepoDto2.htmlUrl = "https://github.com/octocat/repo-2";

        githubRepoDtos = new GithubRepoDto[] { githubRepoDto1, githubRepoDto2 };
    }

}
