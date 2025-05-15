package com.official.memento.auth.controller.dto;

import com.official.memento.auth.domain.AuthProvider;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import io.swagger.v3.oas.annotations.media.Schema;
import com.official.memento.global.util.Validator;

import java.time.ZoneOffset;

@Schema(name = "소셜로그인 요청")
public record AuthApiRequest(
        @Schema(description = "Social Login Provider", example = "APPLE,GOOGLE")
        AuthProvider provider,
        @Schema(description = "OAuth ID Token", example = "eysdsdsd~")
        String idToken,
        @Schema(description = "Timezone Offset", example = "+09:00")
        String timeZoneOffset,
        @Schema(description = "FCM Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
        String fcmToken
) {
    public AuthApiRequest(
            final AuthProvider provider,
            final String idToken,
            final String timeZoneOffset,
            final String fcmToken
    ) {
        validate(provider, idToken, fcmToken,timeZoneOffset);
        this.provider = provider;
        this.idToken = idToken;
        this.timeZoneOffset = timeZoneOffset;
        this.fcmToken = fcmToken;
    }

    private static void validate(final AuthProvider provider, final String idToken, final String fcmToken,final String timeZoneOffset) {
        try {
            ZoneOffset.of(timeZoneOffset);
        } catch (Error e) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_REQUEST_BODY);
        }
        Validator.isNull(provider);
        Validator.isNull(idToken);
        Validator.isNull(fcmToken);
    }
}