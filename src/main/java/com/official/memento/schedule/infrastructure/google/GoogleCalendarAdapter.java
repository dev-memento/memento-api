package com.official.memento.schedule.infrastructure.google;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
import com.official.memento.schedule.domain.entity.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
@AllArgsConstructor
public class GoogleCalendarAdapter {

    private final RestClient googleRestClient;

    public GoogleCalendarResponse getCalendarEvents(final String accessToken, final String syncToken) {
        try {
            return fetchCalendarEvents(accessToken, syncToken);
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.GONE) { //싱크 토큰 만료로 인한 재요청
                return fetchCalendarEvents(accessToken, null);
            } else if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {     //토큰 만료
                throw new MementoException(ErrorCode.INVALID_ACCESS_TOKEN);
            } else {
                throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        } catch (RestClientException ex) {  //기타 통신 오류 처리, 타임아웃 및 연결 에러
            throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateCalendarEvent(final String accessToken, final Schedule schedule) {
        GoogleCalendarUpdateRequest body = GoogleCalendarUpdateRequest.of(schedule);

        try {
            ResponseEntity<Void> response = googleRestClient
                    .put()
                    .uri("/calendars/primary/events")
                    .headers(headers -> headers.setBearerAuth(accessToken))
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        } catch (RestClientException ex) {  //기타 통신 오류 처리
            throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private GoogleCalendarResponse fetchCalendarEvents(final String accessToken, final String syncToken) {
        URI uri = buildGoogleEventsUri(syncToken);

        ResponseEntity<GoogleCalendarResponse> response = googleRestClient
                .get()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(GoogleCalendarResponse.class);

        return Optional.ofNullable(response.getBody())
                .orElseThrow(() -> new MementoException(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private URI buildGoogleEventsUri(final String syncToken) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromPath("/calendars/primary/events")
                .queryParam("singleEvents", "true");

        if (syncToken == null) {
            builder.queryParam("showDeleted", "true");
        } else {
            builder.queryParam("showDeleted", "false")
                    .queryParam("syncToken", syncToken);
        }

        return builder.build().toUri();
    }
}