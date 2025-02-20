package com.official.memento.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "소셜로그인 응답")
public record LoginResponse(
        @Schema(description = "발급된 Access Token", example = "Bearer dsdsdsds")
        String accessToken,
        @Schema(description = "발급된 Refresh Token", example = "Bearer dsdsdsds")
        String refreshToken,
        @Schema(description = "신규 사용자 및 온보딩 완료 여부", example = "true")
        boolean isNewUser
) {}
