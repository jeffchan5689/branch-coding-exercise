package com.branch.exercise.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepoDto {
    public String name;

    @JsonProperty("html_url")
    public String htmlUrl;
}
