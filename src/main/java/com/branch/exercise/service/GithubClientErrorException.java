package com.branch.exercise.service;

public class GithubClientErrorException extends RuntimeException {

    public GithubClientErrorException(String httpMessage) {
        super("Bad request communicating with Github: " + httpMessage);
    }

}
