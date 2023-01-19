package com.branch.exercise.service;

public class NoGithubUserException extends RuntimeException {

    public NoGithubUserException(String username) {
        super("No Github user found for username " + username);
    }
}
