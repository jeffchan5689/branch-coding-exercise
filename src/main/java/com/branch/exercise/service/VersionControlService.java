package com.branch.exercise.service;

import com.branch.exercise.controller.dto.VersionControlUserDto;
import com.branch.exercise.gateway.GithubGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VersionControlService {

    @Autowired
    GithubGateway githubGateway;

    @Cacheable("username")
    public VersionControlUserDto getUserData(String username) {
        VersionControlUserDto versionControlUser = githubGateway.getGithubUserForUsername(username);

        versionControlUser.repos = githubGateway.getGithubReposForUser(username);

        return versionControlUser;
    }
}
