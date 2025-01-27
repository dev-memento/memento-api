package com.official.memento.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "소셜로그인 응답")
public record AuthApiResponse(
        @Schema(description = "발급된 Access Token", example = "Bearer dsdsdsds")
        String accessToken,
        @Schema(description = "발급된 Refresh Token", example = "Bearer dsdsdsds")
        String refreshToken,
        @Schema(description = "신규 사용자 여부", example = "true")
        boolean isNewUser
) {

    public AuthApiResponse of(
            String accessToken,
            String refreshToken,
            boolean isNewUser
    ) {
        return new AuthApiResponse(accessToken, refreshToken, isNewUser);
    }
}