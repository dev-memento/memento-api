package com.official.memento.auth.controller;

import com.official.memento.auth.controller.dto.AuthApiRequest;
import com.official.memento.auth.controller.dto.LoginResponse;
import com.official.memento.auth.controller.dto.TokenRefreshResponse;
import com.official.memento.auth.service.AuthResultDto;
import com.official.memento.auth.service.dto.MemberInfoDto;
import com.official.memento.global.dto.ErrorResponse;
import com.official.memento.global.dto.SuccessResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "[Auth] 인증 관련 API")
public interface AuthApiDocs {

    @Operation(summary = "소셜 로그인 API", description = "소셜 로그인 요청을 처리하여 액세스 및 리프레시 토큰을 발급합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "소셜 로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content)
            }
    )
    ResponseEntity<SuccessResponse<MemberInfoDto>> login(
            @Parameter(name = "AuthApiRequest", description = "로그인 요청 DTO", required = true, schema = @Schema(implementation = AuthApiRequest.class))
            @RequestBody final AuthApiRequest request);

    @Operation(summary = "토큰 갱신 API", description = "리프레시 토큰을 사용하여 새로운 액세스 및 리프레시 토큰을 발급합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "리프레시 토큰 유효하지 않음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content)
            }
    )
    ResponseEntity<SuccessResponse<AuthResultDto>> refreshTokens(
            @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 리프레시 토큰", required = true, example = "Bearer refresh_token")
            @RequestHeader String authorizationHeader);
}