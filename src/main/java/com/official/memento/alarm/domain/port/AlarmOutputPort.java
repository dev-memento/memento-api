package com.official.memento.alarm.domain.port;

public interface AlarmOutputPort {
    void sendAlarm(String uri, String content);
    void sendExceptionAlarm(Exception e);
}
