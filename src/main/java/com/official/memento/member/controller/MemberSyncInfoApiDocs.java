package com.official.memento.member.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.member.controller.dto.MemberPersonalInfoRequest;
import com.official.memento.member.service.dto.MemberSyncInfoDto;
import com.official.memento.member.service.dto.MemberUptimeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "[MemberSyncInfo] 사용자 연동 정보 관련 API")
public interface MemberSyncInfoApiDocs {

    @Operation(summary = "사용자 연동 정보 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "사용자 업타임 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberSyncInfoDto.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
                    @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content)
            }
    )
    ResponseEntity<SuccessResponse<MemberSyncInfoDto>> getUpTime(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer Token", required = true, example = "Bearer access_token")
            @Authorization final AuthorizationUser authorizationUser);
}
