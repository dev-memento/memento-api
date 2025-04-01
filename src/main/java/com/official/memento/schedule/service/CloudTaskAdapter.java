package com.official.memento.schedule.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.tasks.v2.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class CloudTaskAdapter {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.location-id}")
    private String locationId;

    @Value("${gcp.queue-id}")
    private String queueId;

    @Value("${gcp.target-url}")
    private String targetUrl;

    @Value("${gcp.service-account-key-path}")
    private String serviceAccountKeyPath;

    public void createTask(String scheduleId) throws IOException {
        ZonedDateTime executeTime = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(10);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(serviceAccountKeyPath));
        CloudTasksSettings settings = CloudTasksSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (CloudTasksClient client = CloudTasksClient.create(settings)) {
            String queuePath = QueueName.of(projectId, locationId, queueId).toString();

            Instant instant = executeTime.toInstant();
            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(instant.getEpochSecond())
                    .setNanos(instant.getNano())
                    .build();

            String payload = "{\"scheduleId\":\"" + scheduleId + "\"}";

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .setUrl(targetUrl)
                    .setHttpMethod(HttpMethod.POST)
                    .putHeaders("Content-Type", "application/json")
                    .setBody(ByteString.copyFromUtf8(payload))
                    .build();

            Task task = Task.newBuilder()
                    .setHttpRequest(httpRequest)
                    .setScheduleTime(timestamp)
                    .build();

            client.createTask(queuePath, task);
        }
    }
}