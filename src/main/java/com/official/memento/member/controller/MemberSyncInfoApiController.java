package com.official.memento.member.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.member.controller.dto.MemberSyncInfoDto;
import com.official.memento.member.service.command.MemberSyncInfoGetUseCase;
import com.official.memento.member.service.result.MemberSyncInfoResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/sync-info")
public class MemberSyncInfoApiController {

    private final MemberSyncInfoGetUseCase memberSyncInfoGetUseCase;

    @GetMapping
    public ResponseEntity<SuccessResponse<MemberSyncInfoDto>> getMemberSyncInfo(@Authorization AuthorizationUser authorizationUser) {
        MemberSyncInfoResult result = memberSyncInfoGetUseCase.getMemberSync(authorizationUser.memberId());
        MemberSyncInfoDto response = new MemberSyncInfoDto(result.isAppleSync(), !result.googleSyncToken().isEmpty());
        return SuccessResponse.of(HttpStatus.OK, "사용자 연동 정보 조회 성공", response);
    }
}
