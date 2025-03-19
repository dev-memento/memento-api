package com.official.memento.schedule.infrastructure.google;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@AllArgsConstructor
public class GoogleCalendarAdapter {

    private final RestTemplate restTemplate;

    public List<GoogleCalendarEvent> getCalendarEvents(final String accessToken) {
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleCalendarResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GoogleCalendarResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().items();
        } else {
            throw new RuntimeException("Failed to fetch calendar events: " + response.getStatusCode());
        }
    }
}
