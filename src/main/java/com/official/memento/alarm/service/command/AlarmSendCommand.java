package com.official.memento.alarm.service.command;

public record AlarmSendCommand(
        String uri,
        String content
) {
}
