package com.branch.exercise.service;

public class GithubServerErrorException extends RuntimeException {

    public GithubServerErrorException(String httpMessage) {
        super("Error communicating with Github: " + httpMessage);
    }
}
