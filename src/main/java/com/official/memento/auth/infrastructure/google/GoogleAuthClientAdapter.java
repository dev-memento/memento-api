package com.official.memento.auth.infrastructure.google;

import com.official.memento.auth.domain.port.AuthClientOutputPort;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidIdTokenException;
import com.official.memento.global.exception.MementoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleAuthClientAdapter implements AuthClientOutputPort {

    private final RestTemplate restTemplate;
    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;

    @Override
    public Map<String, Object> verifyIdToken(final String idToken) {
        final String tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        final ResponseEntity<Map> response = restTemplate.getForEntity(tokenInfoUrl, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new InvalidIdTokenException(ErrorCode.INVALID_ID_TOKEN);
        }

        SuccessResponse.of(HttpStatus.OK, "ID Token verified successfully", response.getBody());

        return response.getBody();
    }

    @Override
    public String refreshAccessToken(final String refreshToken) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        // 1. 요청 Body 구성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        // 2. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // 3. 요청 실행
        ResponseEntity<GoogleTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                GoogleTokenResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().accessToken();
        } else {
            throw new RuntimeException("Failed to refresh access token: " + response.getStatusCode());//이때 로그아웃 발생해야 함
        }
    }
}
