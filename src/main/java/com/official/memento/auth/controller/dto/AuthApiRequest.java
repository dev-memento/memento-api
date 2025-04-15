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
        @Schema(description = "Timezone Offset", example = "9")
        int timeZoneOffset,
        @Schema(description = "FCM Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
        String fcmToken
) {
    public AuthApiRequest(
            final AuthProvider provider,
            final String idToken,
            final int timeZoneOffset,
            final String fcmToken
    ) {
        validate(provider, idToken, fcmToken);
        this.provider = provider;
        this.idToken = idToken;
        this.timeZoneOffset = timeZoneOffset;
        this.fcmToken = fcmToken;
    }

    private static void validate(final AuthProvider provider, final String idToken, final String fcmToken) {
        Validator.isNull(provider);
        Validator.isNull(idToken);
        Validator.isNull(fcmToken);
    }
}