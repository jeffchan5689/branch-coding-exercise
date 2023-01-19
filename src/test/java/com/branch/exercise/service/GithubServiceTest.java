package com.branch.exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.branch.exercise.controller.dto.VersionControlDto;
import com.branch.exercise.gateway.GithubGateway;
import com.branch.exercise.gateway.dto.GithubRepoDto;
import com.branch.exercise.gateway.dto.GithubUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GithubServiceTest {

    @Mock
    private GithubGateway githubGateway;

    @InjectMocks
    private GithubService githubService;

    private GithubUserDto githubUserDto;

    private List<GithubRepoDto> githubRepoDtos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        setDefaultObjects();

        when(githubGateway.getGithubUserForUsername(eq("user"))).thenReturn(githubUserDto);
        when(githubGateway.getGithubReposForUser(eq("user"))).thenReturn(githubRepoDtos);
    }

    @Test
    public void gettingUserDataSetsUserInformationCorrectly() {
        VersionControlDto result = githubService.getUserData("user");

        verify(githubGateway).getGithubUserForUsername(eq("user"));

        assertEquals("octocat", result.userName);
        assertEquals("The Octocat", result.displayName);
        assertEquals("https://avatars3.githubusercontent.com/u/583231?v=4", result.avatar);
        assertEquals("San Francisco", result.geoLocation);
        assertEquals("octocat@github.com", result.email);
        assertEquals("https://github.com/octocat", result.url);
        assertEquals(OffsetDateTime.of(LocalDateTime.of(2011, 1, 25, 18, 44, 36), ZoneOffset.UTC).toLocalDateTime(), result.createdAt);
        assertEquals(2, result.repos.size());
    }

    @Test
    public void gettingUserDataSetsUserRepoInformationCorrectlyForEachReturnedRepo() {
        VersionControlDto result = githubService.getUserData("user");

        verify(githubGateway).getGithubReposForUser(eq("user"));

        assertEquals("repo1", result.repos.get(0).name);
        assertEquals("https://github.com/octocat/repo-1", result.repos.get(0).url);
        assertEquals("repo2", result.repos.get(1).name);
        assertEquals("https://github.com/octocat/repo-2", result.repos.get(1).url);
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

        githubRepoDtos.add(githubRepoDto1);
        githubRepoDtos.add(githubRepoDto2);
    }
}
