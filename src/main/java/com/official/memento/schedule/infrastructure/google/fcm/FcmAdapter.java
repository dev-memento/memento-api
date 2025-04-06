package com.official.memento.schedule.infrastructure.google.fcm;

import static com.official.memento.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.official.memento.global.exception.MementoException;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class FcmAdapter {

    public void sendNotification(
            final String targetToken,
            final String description,
            final LocalTime startTime,
            final LocalTime endTime
    ) {
        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(
                        Notification.builder()
                                .setTitle("15분 전 알람")
                                .setBody(startTime + " ~ " + endTime + " " + description)
                                .build()
                )
                .putData("type", "ALARM")
                .build();


        try {
            String response = FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            throw new MementoException(INTERNAL_SERVER_ERROR);
        }
    }
}
