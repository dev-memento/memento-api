package com.official.memento.member.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.member.controller.dto.MemberPersonalInfoRequest;
import com.official.memento.member.controller.dto.MemberTimeZoneUpdateRequest;
import com.official.memento.member.controller.dto.MemberUptimeDto;
import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.service.command.MemberPersonalInfoCommand;
import com.official.memento.member.service.usecase.MemberPersonalInfoRetrieveUseCase;
import com.official.memento.member.service.usecase.MemberPersonalInfoUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/personal-info")
public class MemberPersonalInfoApiController implements MemberPersonalInfoApiDocs {
    private final MemberPersonalInfoUpdateUseCase memberPersonalInfoUpdateUseCase;
    private final MemberPersonalInfoRetrieveUseCase memberPersonalInfoRetrieveUseCase;

    @PatchMapping
    @Override
    public ResponseEntity<SuccessResponse<?>> updatePersonalInfo(
            @Authorization AuthorizationUser authorizationUser,
            @RequestBody final MemberPersonalInfoRequest request) {
        memberPersonalInfoUpdateUseCase.update(
                MemberPersonalInfoCommand.of(
                        authorizationUser.memberId(),
                        request.wakeUpTime(),
                        request.windDownTime(),
                        request.job(),
                        request.jobOtherDetail(),
                        request.isStressedUnorganizedSchedule(),
                        request.isForgetImportantThings(),
                        request.isPreferReminder(),
                        request.isImportantBreaks())
        );
        return SuccessResponse.of(HttpStatus.OK, "회원 개인 정보 업데이트 성공");
    }

    @GetMapping("/uptime")
    public ResponseEntity<SuccessResponse<MemberUptimeDto>> getUpTime(
            @Authorization final AuthorizationUser authorizationUser) {
        final MemberPersonalInfo personalInfo = memberPersonalInfoRetrieveUseCase.retrieveUptime(authorizationUser.memberId());
        final MemberUptimeDto response = MemberUptimeDto.of(
                personalInfo.getWakeUpTime().toString(),
                personalInfo.getWindDownTime().toString()
        );
        return SuccessResponse.of(HttpStatus.OK, "사용자 업타임 조회 성공", response);
    }

    @PatchMapping("/timezone")
    public ResponseEntity<SuccessResponse<?>> updateTimeZone(
            @Authorization AuthorizationUser authorizationUser,
            @RequestBody final MemberTimeZoneUpdateRequest request) {
        memberPersonalInfoUpdateUseCase.updateTimeZone(authorizationUser.memberId(), request.timeZoneOffset());
        return SuccessResponse.of(HttpStatus.OK, "사용자 시간대 업데이트 성공");
    }
}
