package com.official.memento.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "사용자 업타임 조회 응답")
public record MemberUptimeDto(
        @Schema(description = "기상 시간", example = "06:30")
        String wakeUpTime,
        @Schema(description = "일정 마무리 시간", example = "22:30")
        String windDownTime
) {
    public static MemberUptimeDto of(String wakeUpTime, String windDownTime) {
        return new MemberUptimeDto(wakeUpTime, windDownTime);
    }
}
