package com.official.memento.auth.controller.dto;

import com.official.memento.auth.domain.AuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import com.official.memento.global.util.Validator;

@Schema(name = "소셜로그인 요청")
public record AuthApiRequest(
        @Schema(description = "Social Login Provider", example = "APPLE,GOOGLE")
        AuthProvider provider,
        @Schema(description = "OAuth ID Token", example = "eysdsdsd~")
        String idToken,
        String authorizationCode,
        int timeZoneOffset
) {
    public AuthApiRequest(final AuthProvider provider,final String idToken,final String authorizationCode,final int timeZoneOffset) {
        validate(provider, idToken, authorizationCode);
        this.provider = provider;
        this.idToken = idToken;
        this.authorizationCode = authorizationCode;
        this.timeZoneOffset = timeZoneOffset;
    }

    private static void validate(AuthProvider provider, String idToken, String authorizationCode) {
        Validator.isNull(provider);
        Validator.isNull(idToken);
    }
}