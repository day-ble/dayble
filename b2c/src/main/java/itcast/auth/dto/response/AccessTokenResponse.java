package itcast.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public record AccessTokenResponse(
        @JsonProperty(value = "access_token", required = true)
        String accessToken
) {}