package com.official.memento.auth.controller;

import com.official.memento.auth.controller.dto.AuthApiRequest;
import com.official.memento.auth.controller.dto.LoginResponse;
import com.official.memento.auth.controller.dto.TokenRefreshResponse;
import com.official.memento.auth.service.AuthResult;
import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.usecase.AuthenticateUseCase;
import com.official.memento.auth.service.usecase.ExtractRefreshTokenUseCase;
import com.official.memento.auth.service.usecase.RefreshTokenUseCase;
import com.official.memento.global.dto.SuccessResponse;
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
public class AuthApiController implements AuthApiDocs {

    private final AuthenticateUseCase authenticateUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final ExtractRefreshTokenUseCase extractRefreshTokenUseCase;

    public AuthApiController(
            AuthenticateUseCase authenticateUseCase,
            RefreshTokenUseCase refreshTokenUseCase,
            ExtractRefreshTokenUseCase extractRefreshTokenUseCase
    ) {

        this.authenticateUseCase = authenticateUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.extractRefreshTokenUseCase = extractRefreshTokenUseCase;
    }

    @Override
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@Valid @RequestBody final AuthApiRequest request) {
        final AuthCommand command = AuthCommand.of(request.provider(), request.idToken());
        final AuthResult authResult = authenticateUseCase.authenticate(command);

        final LoginResponse response = new LoginResponse(
                authResult.getAccessToken().getToken(),
                authResult.getRefreshToken().getToken(),
                authResult.isNewUser()
        );
        return SuccessResponse.of(HttpStatus.OK, "소셜 로그인 성공", response);
    }

    @Override
    @PostMapping("/api/v1/auth/token/refresh")
    public ResponseEntity<SuccessResponse<TokenRefreshResponse>> refreshTokens(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        AuthValidation.validateBearerRefreshToken(authorizationHeader);
        String refreshToken = extractRefreshTokenUseCase.extractRefreshToken(authorizationHeader);
        AuthResult authResult = refreshTokenUseCase.refreshTokens(refreshToken);

        TokenRefreshResponse response = new TokenRefreshResponse(
                authResult.getAccessToken().getToken(),
                authResult.getRefreshToken().getToken()
        );
        return SuccessResponse.of(HttpStatus.OK, "토큰 갱신 성공", response);
    }
}
