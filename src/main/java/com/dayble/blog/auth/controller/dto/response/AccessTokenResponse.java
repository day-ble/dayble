package com.dayble.blog.auth.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenResponse(
        @JsonProperty(value = "access_token", required = true)
        String accessToken
) {}
