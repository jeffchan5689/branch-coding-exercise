package com.branch.exercise.service;

import com.branch.exercise.controller.dto.RepoDto;
import com.branch.exercise.controller.dto.VersionControlDto;
import com.branch.exercise.gateway.GithubGateway;
import com.branch.exercise.gateway.dto.GithubRepoDto;
import com.branch.exercise.gateway.dto.GithubUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GithubService {

    @Autowired
    GithubGateway githubGateway;

    public VersionControlDto getUserData(String username) {
        VersionControlDto versionControlUser = mapGithubUserToVersionControlUser(githubGateway.getGithubUserForUsername(username));

        versionControlUser.repos =
                githubGateway.getGithubReposForUser(username)
                        .stream()
                        .map(this::mapGithubReposToVersionControlRepo)
                        .collect(Collectors.toList());

        return versionControlUser;
    }

    private VersionControlDto mapGithubUserToVersionControlUser(GithubUserDto githubUserDto) {
        VersionControlDto versionControlDto = new VersionControlDto();

        versionControlDto.userName = githubUserDto.login;
        versionControlDto.displayName = githubUserDto.name;
        versionControlDto.avatar = githubUserDto.avatarUrl;
        versionControlDto.geoLocation = githubUserDto.geoLocation;
        versionControlDto.email = githubUserDto.email;
        versionControlDto.url = githubUserDto.url;
        versionControlDto.createdAt = githubUserDto.createdAt.toLocalDateTime();

        return versionControlDto;
    }

    private RepoDto mapGithubReposToVersionControlRepo(GithubRepoDto githubRepoDto) {
        RepoDto repoDto = new RepoDto();

        repoDto.name = githubRepoDto.name;
        repoDto.url = githubRepoDto.htmlUrl;

        return repoDto;
    }
}
