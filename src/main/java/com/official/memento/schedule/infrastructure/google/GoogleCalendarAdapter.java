package com.official.memento.schedule.infrastructure.google;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.ExpiredTokenException;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.global.exception.MementoException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@AllArgsConstructor
public class GoogleCalendarAdapter {

    private final RestTemplate restTemplate;

    public GoogleCalendarResponse getCalendarEvents(final String accessToken,final String syncToken) {
        final String GOOGLE_CALENDAR_EVENTS_URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        HttpHeaders headers = createAuthHeaders(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleCalendarResponse> response = restTemplate.exchange(
                GOOGLE_CALENDAR_EVENTS_URL,
                HttpMethod.GET,
                request,
                GoogleCalendarResponse.class
        );

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            throw new InvalidRequestBodyException(ErrorCode.EXPIRED_TOKEN);
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new MementoException(ErrorCode.EXTERNAL_SERVER_ERROR);
        }

        return response.getBody();
    }

    private HttpHeaders createAuthHeaders(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }
}
