package com.official.memento.schedule.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.tasks.v2.CloudTasksClient;
import com.google.cloud.tasks.v2.CloudTasksSettings;
import com.google.cloud.tasks.v2.HttpMethod;
import com.google.cloud.tasks.v2.HttpRequest;
import com.google.cloud.tasks.v2.QueueName;
import com.google.cloud.tasks.v2.Task;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
import com.official.memento.schedule.domain.entity.ScheduleAlarm;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CloudTaskAdapter {

    @Value("${GCP.PROJECT_ID}")
    private String projectId;

    @Value("${GCP.LOCATION_ID}")
    private String locationId;

    @Value("${GCP.QUEUE_ID}")
    private String queueId;

    @Value("${GCP.TARGET_URL}")
    private String targetUrl;

    @Value("${ADMIN.TOKEN_PREFIX}")
    private String AUTHORIZATION_HEADER_ADMIN_PREFIX;


    public void createScheduleAlarm(final ScheduleAlarm scheduleAlarm) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
                ));
        CloudTasksSettings settings = CloudTasksSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        LocalDateTime executeTime = scheduleAlarm.getStartDate().minusMinutes(15);

        try (CloudTasksClient client = CloudTasksClient.create(settings)) {
            String queuePath = QueueName.of(projectId, locationId, queueId).toString();

            Instant instant = executeTime.toInstant(ZoneOffset.UTC);
            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(instant.getEpochSecond())
                    .setNanos(instant.getNano())
                    .build();

            String payload = "{"
                    + "\"description\":\"" + scheduleAlarm.getDescription() + "\","
                    + "\"memberId\":" + scheduleAlarm.getMemberId()
                    + "}";

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .setUrl(targetUrl)
                    .setHttpMethod(HttpMethod.POST)
                    .putHeaders("Content-Type", "application/json")
                    .putHeaders("Authorization","Bearer " + AUTHORIZATION_HEADER_ADMIN_PREFIX + scheduleAlarm.getMemberId())
                    .setBody(ByteString.copyFromUtf8(payload))
                    .build();

            Task task = Task.newBuilder()
                    .setHttpRequest(httpRequest)
                    .setScheduleTime(timestamp)
                    .build();

            client.createTask(queuePath, task);
        }catch (Exception e) {
            throw new MementoException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}