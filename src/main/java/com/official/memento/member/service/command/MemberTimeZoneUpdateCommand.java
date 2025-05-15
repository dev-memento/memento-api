package com.official.memento.member.service.command;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;

import java.time.ZoneOffset;

public record MemberTimeZoneUpdateCommand(
        long memberId,
        String timeZoneOffset
) {
    public static MemberTimeZoneUpdateCommand of(
            final long memberId,
            final String timeZoneOffset
    ) {
        try{
            ZoneOffset.of(timeZoneOffset);
        } catch (Exception e) {
            throw new InvalidRequestBodyException(
                    ErrorCode.INVALID_REQUEST_BODY
            );
        }
        return new MemberTimeZoneUpdateCommand(
                memberId,
                timeZoneOffset
        );
    }
}
