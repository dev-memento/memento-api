package com.official.memento.alarm.infrastructure.client;

import com.official.memento.alarm.domain.port.AlarmOutputPort;
import com.official.memento.global.stereotype.Adapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Adapter
@RequiredArgsConstructor
public class AlarmSendClientAdapter implements AlarmOutputPort {

    private final WebClient webClient;

    @Value("${discord.error-alert-url}")
    private String discordErrorAlarmUri;

    @Override
    @Async
    public void sendAlarm(String uri, String content) {
        webClient.post()
                .uri(uri)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(new AlarmRequestBody(content, null))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    @Override
    @Async
    public void sendExceptionAlarm(Exception e) {
        webClient.post()
                .uri(discordErrorAlarmUri)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(new AlarmRequestBody(
                        "에러 발생 : " + e.getMessage() + " ( " + e.getClass().getSimpleName() + " )",
                        List.of(new Embed(
                                "Stack Trace",
                                parseStackTrace(e)
                        ))
                ))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    private String parseStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();

        if (stackTrace.length() > 2000) {
            return stackTrace.substring(0, 2000);
        }
        return stackTrace;
    }

    record AlarmRequestBody(
            String content,
            List<Embed> embeds
    ) {}

    record Embed(
            String title,
            String description
    ) {}
}
