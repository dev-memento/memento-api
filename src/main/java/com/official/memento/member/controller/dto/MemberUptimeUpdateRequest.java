package com.official.memento.member.controller.dto;

import java.time.LocalTime;

public record MemberUptimeUpdateRequest(
        String wakeUpTime
) {
}
