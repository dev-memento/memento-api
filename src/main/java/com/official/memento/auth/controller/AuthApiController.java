package com.official.memento.auth.controller;

import com.official.memento.auth.service.result.AuthResult;
import com.official.memento.auth.service.usecase.RefreshTokenUseCase;
import com.official.memento.auth.util.AuthValidation;
import com.official.memento.global.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiController implements AuthApiDocs {

    private final RefreshTokenUseCase refreshTokenUseCase;

    @Override
    @PostMapping("/api/v1/auth/token/refresh")
    public ResponseEntity<SuccessResponse<AuthResult>> refreshTokens(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        AuthValidation.validateBearerRefreshToken(authorizationHeader);
        AuthResult response = refreshTokenUseCase.refreshTokens(authorizationHeader);
        return SuccessResponse.of(HttpStatus.OK, "토큰 갱신 성공", response);
    }
}
