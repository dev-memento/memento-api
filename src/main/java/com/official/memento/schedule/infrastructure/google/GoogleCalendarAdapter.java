package com.official.memento.schedule.infrastructure.google;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@AllArgsConstructor
public class GoogleCalendarAdapter {

    private final RestTemplate restTemplate;

    public GoogleCalendarResponse getCalendarEvents(final String accessToken, final String syncToken) {
        ResponseEntity<GoogleCalendarResponse> response = getGoogleCalendarResponseResponseEntity(
                accessToken,
                syncToken
        );

        if (response.getStatusCode() == HttpStatus.GONE) {
            response = getGoogleCalendarResponseResponseEntity(accessToken, null);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new MementoException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        return response.getBody();
    }

    @NotNull
    private ResponseEntity<GoogleCalendarResponse> getGoogleCalendarResponseResponseEntity(
            String accessToken,
            String syncToken) {
        final String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("showDeleted", "true")
                .queryParam("singleEvents", "true");
        if (syncToken != null) {
            builder.queryParam("syncToken", syncToken).queryParam("showDeleted", "false");
        }

        HttpHeaders headers = createAuthHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleCalendarResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                request,
                GoogleCalendarResponse.class
        );
        return response;
    }

    private HttpHeaders createAuthHeaders(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }
}
