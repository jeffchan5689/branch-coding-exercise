package com.branch.exercise.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class VersionControlDto {

    @JsonProperty("user_name")
    public String userName;

    @JsonProperty("display_name")
    public String displayName;

    public String avatar;

    @JsonProperty("geo_location")
    public String geoLocation;

    public String email;

    public String url;

    @JsonProperty("created_at")
    public LocalDateTime createdAt;

    public List<RepoDto> repos;
}
