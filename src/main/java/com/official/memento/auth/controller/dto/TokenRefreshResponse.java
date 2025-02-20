package com.official.memento.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "토큰 갱신 응답")
public record TokenRefreshResponse(
        @Schema(description = "발급된 Access Token", example = "Bearer dsdsdsds")
        String accessToken,
        @Schema(description = "발급된 Refresh Token", example = "Bearer dsdsdsds")
        String refreshToken
) {}