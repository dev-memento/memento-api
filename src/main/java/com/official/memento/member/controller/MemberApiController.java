package com.official.memento.member.controller;

import com.official.memento.auth.controller.dto.AuthApiRequest;
import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.result.NewAuthResult;
import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.member.service.command.MemberDeleteCommand;
import com.official.memento.member.service.usecase.MemberUpdateUseCase;
import com.official.memento.member.service.usecase.MemberDeleteUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberApiController implements MemberApiControllerDocs {

    private final MemberUpdateUseCase memberUpdateUseCase;
    private final MemberDeleteUseCase memberDeleteUseCase;

    @Override
    @PutMapping
    public ResponseEntity<SuccessResponse<NewAuthResult>> login(@Valid @RequestBody final AuthApiRequest request) {
        final AuthCommand command = AuthCommand.of(request.provider(), request.idToken(),request.timeZoneOffset(), request.fcmToken());
        final NewAuthResult response = memberUpdateUseCase.authenticate(command);
        return SuccessResponse.of(HttpStatus.OK, "소셜 로그인 성공", response);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<SuccessResponse<?>> delete(
            @Authorization AuthorizationUser authorizationUser
    ) {
        memberDeleteUseCase.delete(
                MemberDeleteCommand.of(
                        authorizationUser.memberId()
                )
        );
        return SuccessResponse.of(HttpStatus.OK, "회원 탈퇴 성공");
    }
}
