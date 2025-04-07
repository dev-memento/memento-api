package com.official.memento.auth.service;

import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.Auth;
import com.official.memento.auth.domain.RefreshToken;
import com.official.memento.auth.domain.port.AuthRepository;
import com.official.memento.auth.service.result.AuthResult;
import com.official.memento.auth.service.usecase.AuthDeleteUseCase;
import com.official.memento.auth.service.usecase.AuthenticateUseCase;
import com.official.memento.auth.service.usecase.RefreshTokenUseCase;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthenticateUseCase, RefreshTokenUseCase, AuthDeleteUseCase {

    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResult refreshTokens(final String authorizationHeader) {
        String refreshToken = extractRefreshToken(authorizationHeader);
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String memberId = jwtUtil.getUserIdFromToken(refreshToken);
        Auth auth = authRepository.findByMemberId(Long.parseLong(memberId))
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN));
        if (!auth.getRefreshToken().equals(refreshToken)) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        AccessToken newAccessToken = jwtUtil.generateAccessToken(Long.parseLong(memberId));
        RefreshToken newRefreshToken = jwtUtil.generateRefreshToken(Long.parseLong(memberId));
        auth.withUpdatedToken(newRefreshToken.getToken());
        authRepository.save(auth);

        return AuthResult.of(newAccessToken.getToken(), newRefreshToken.getToken());
    }

    private String extractRefreshToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    @Override
    @Transactional
    public void deleteByMemberId(final long memberId) {
        authRepository.deleteByMemberId(memberId);
    }
}


