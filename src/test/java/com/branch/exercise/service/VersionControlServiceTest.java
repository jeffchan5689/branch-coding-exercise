package com.branch.exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.branch.exercise.controller.dto.RepoDto;
import com.branch.exercise.controller.dto.VersionControlUserDto;
import com.branch.exercise.gateway.GithubGateway;
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
public class VersionControlServiceTest {

    @Mock
    private GithubGateway githubGateway;

    @InjectMocks
    private VersionControlService versionControlService;

    private VersionControlUserDto versionControlUserDto;

    private List<RepoDto> repoDtos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        setDefaultObjects();

        when(githubGateway.getGithubUserForUsername(eq("user"))).thenReturn(versionControlUserDto);
        when(githubGateway.getGithubReposForUser(eq("user"))).thenReturn(repoDtos);
    }

    @Test
    public void gettingUserDataReturnssUserInformationCorrectly() {
        VersionControlUserDto result = versionControlService.getUserData("user");

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
        VersionControlUserDto result = versionControlService.getUserData("user");

        verify(githubGateway).getGithubReposForUser(eq("user"));

        assertEquals("repo1", result.repos.get(0).name);
        assertEquals("https://github.com/octocat/repo-1", result.repos.get(0).url);
        assertEquals("repo2", result.repos.get(1).name);
        assertEquals("https://github.com/octocat/repo-2", result.repos.get(1).url);
    }

    private void setDefaultObjects() {
        versionControlUserDto = new VersionControlUserDto();
        versionControlUserDto.userName = "octocat";
        versionControlUserDto.displayName = "The Octocat";
        versionControlUserDto.avatar = "https://avatars3.githubusercontent.com/u/583231?v=4";
        versionControlUserDto.geoLocation = "San Francisco";
        versionControlUserDto.email = "octocat@github.com";
        versionControlUserDto.url = "https://github.com/octocat";
        versionControlUserDto.createdAt = LocalDateTime.of(2011, 1, 25, 18, 44, 36);

        RepoDto githubRepoDto1 = new RepoDto();
        githubRepoDto1.name = "repo1";
        githubRepoDto1.url = "https://github.com/octocat/repo-1";

        RepoDto githubRepoDto2 = new RepoDto();
        githubRepoDto2.name = "repo2";
        githubRepoDto2.url = "https://github.com/octocat/repo-2";

        repoDtos.add(githubRepoDto1);
        repoDtos.add(githubRepoDto2);
    }
}
