package com.official.memento.auth.controller;

import com.official.memento.auth.controller.dto.AuthApiRequest;
import com.official.memento.auth.controller.dto.TokenRefreshResponse;
import com.official.memento.auth.service.AuthResultDto;
import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.dto.MemberInfoDto;
import com.official.memento.auth.service.usecase.AuthenticateUseCase;
import com.official.memento.auth.service.usecase.RefreshTokenUseCase;
import com.official.memento.global.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.official.memento.auth.util.AuthValidation;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthApiController implements AuthApiDocs {

    private final AuthenticateUseCase authenticateUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Override
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<SuccessResponse<MemberInfoDto>> login(@Valid @RequestBody final AuthApiRequest request) {
        final AuthCommand command = AuthCommand.of(request.provider(), request.idToken());
        final MemberInfoDto response = authenticateUseCase.authenticate(command);
        return SuccessResponse.of(HttpStatus.OK, "소셜 로그인 성공", response);
    }

    @Override
    @PostMapping("/api/v1/auth/token/refresh")
    public ResponseEntity<SuccessResponse<AuthResultDto>> refreshTokens(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        AuthValidation.validateBearerRefreshToken(authorizationHeader);
        AuthResultDto response = refreshTokenUseCase.refreshTokens(authorizationHeader);
        return SuccessResponse.of(HttpStatus.OK, "토큰 갱신 성공", response);
    }
}
