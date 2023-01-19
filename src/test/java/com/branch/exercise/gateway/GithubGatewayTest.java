package com.branch.exercise.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.branch.exercise.gateway.dto.GithubRepoDto;
import com.branch.exercise.gateway.dto.GithubUserDto;
import com.branch.exercise.service.NoGithubUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GithubGatewayTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GithubGateway githubGateway;

    private final String GITHUB_BASE_URL = "https://api.github.com";

    @Test
    public void gettingGithubUserReturnsRetrievedData() {
        String username = "octocat";
        GithubUserDto serverResponse = new GithubUserDto();
        serverResponse.login = "octocat";
        serverResponse.name = "The Octocat";

        String fullUrl = GITHUB_BASE_URL + "/users/" + username;
        when(restTemplate.getForObject(fullUrl, GithubUserDto.class)).thenReturn(serverResponse);

        GithubUserDto githubUserDto = githubGateway.getGithubUserForUsername(username);

        assertNotNull(githubUserDto);
        assertNotNull(githubUserDto.login);
        assertNotNull(githubUserDto.name);
    }

    @Test
    public void gettingGithubUserThrowsAnExceptionIfNoLoginIsPresent() {
        String username = "octocat";
        GithubUserDto serverResponse = new GithubUserDto();

        String fullUrl = GITHUB_BASE_URL + "/users/" + username;
        when(restTemplate.getForObject(fullUrl, GithubUserDto.class)).thenReturn(serverResponse);

        NoGithubUserException exception = assertThrows(
                NoGithubUserException.class,
                () -> githubGateway.getGithubUserForUsername(username));

        assertEquals("No Github user found for username: octocat", exception.getMessage());
    }

    @Test
    public void gettingGithubUserReposReturnsRetrievedData() {
        String username = "octocat";

        List<GithubRepoDto> serverResponse = new ArrayList<>();

        GithubRepoDto githubRepoDto1 = new GithubRepoDto();
        githubRepoDto1.name = "repo1";
        githubRepoDto1.htmlUrl = "https://github.com/octocat/repo-1";

        GithubRepoDto githubRepoDto2 = new GithubRepoDto();
        githubRepoDto2.name = "repo2";
        githubRepoDto2.htmlUrl = "https://github.com/octocat/repo-2";

        serverResponse.add(githubRepoDto1);
        serverResponse.add(githubRepoDto2);

        String fullUrl = GITHUB_BASE_URL + "/users/" + username + "/repos";
        when(restTemplate.getForObject(fullUrl, GithubRepoDto[].class)).thenReturn(new GithubRepoDto[] { githubRepoDto1, githubRepoDto2});

        List<GithubRepoDto> repoDtoList = githubGateway.getGithubReposForUser(username);

        assertEquals("repo1", repoDtoList.get(0).name);
        assertEquals("https://github.com/octocat/repo-1", repoDtoList.get(0).htmlUrl);
        assertEquals("repo2", repoDtoList.get(1).name);
        assertEquals("https://github.com/octocat/repo-2", repoDtoList.get(1).htmlUrl);
    }
}
