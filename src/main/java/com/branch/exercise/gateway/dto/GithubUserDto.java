package com.branch.exercise.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class GithubUserDto {

    public String login;

    public String name;

    @JsonProperty("avatar_url")
    public String avatarUrl;

    @JsonProperty("geo_location")
    public String geoLocation;

    public String email;

    @JsonProperty("html_url")
    public String url;

    @JsonProperty("created_at")
    public OffsetDateTime createdAt;
}
