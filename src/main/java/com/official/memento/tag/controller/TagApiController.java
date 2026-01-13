package com.official.memento.tag.controller;

import com.official.memento.global.annotation.Authorization;
import com.official.memento.global.annotation.AuthorizationUser;
import com.official.memento.global.dto.SuccessResponse;
import com.official.memento.tag.controller.dto.TagCreateRequest;
import com.official.memento.tag.controller.dto.TagResponse;
import com.official.memento.tag.controller.dto.TagGetResponse;
import com.official.memento.tag.controller.dto.TagUpdateRequest;
import com.official.memento.tag.service.result.TagResult;
import com.official.memento.tag.service.usecase.TagCreateUseCase;
import com.official.memento.tag.service.usecase.TagDeleteUseCase;
import com.official.memento.tag.service.usecase.TagGetUseCase;
import com.official.memento.tag.service.usecase.TagUpdateUseCase;
import com.official.memento.tag.service.command.TagCreateCommand;
import com.official.memento.tag.service.command.TagDeleteCommand;
import com.official.memento.tag.service.command.TagUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagApiController implements TagApiDocs {

    private final TagCreateUseCase tagCreateUseCase;
    private final TagGetUseCase tagGetUseCase;
    private final TagUpdateUseCase tagUpdateUseCase;
    private final TagDeleteUseCase tagDeleteUseCase;

    @Override
    @PostMapping
    public ResponseEntity<SuccessResponse<TagResponse>> createTag(
            @Authorization final AuthorizationUser authorizationUser,
            @RequestBody final TagCreateRequest request
    ) {
        TagResult tag = tagCreateUseCase.create(
                TagCreateCommand.createWithHexColor(
                        authorizationUser.memberId(),
                        request.hexCode(),
                        request.name()
                )
        );
        return SuccessResponse.of(
                HttpStatus.OK,
                "Tag 생성 성공",
                TagResponse.of(tag.id(),tag.name(), tag.color())
        );
    }

    @Override
    @GetMapping
    public ResponseEntity<SuccessResponse<List<TagGetResponse>>> getTags(
            @Authorization final AuthorizationUser authorizationUser
    ) {
        final List<TagResult> tags = tagGetUseCase.getTags(authorizationUser.memberId());
        return SuccessResponse.of(
                HttpStatus.OK,
                "Tag 조회 성공",
                tags.stream().map(tag -> TagGetResponse.of(tag.id(), tag.name(), tag.color().getHexCode())).toList()
        );
    }

    @Override
    @PatchMapping("/{tagId}")
    public ResponseEntity<SuccessResponse<TagResponse>> updateTag(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long tagId,
            @RequestBody final TagUpdateRequest tagUpdateRequest
    ) {
        TagResult tag = tagUpdateUseCase.update(TagUpdateCommand.updateWithHexColor(
                authorizationUser.memberId(),
                tagId,
                tagUpdateRequest.hexCode(),
                tagUpdateRequest.name()
        ));
        return SuccessResponse.of(
                HttpStatus.OK,
                "태그 업데이트 성공",
                TagResponse.of(tag.id(),tag.name(), tag.color())
        );

    }

    @Override
    @DeleteMapping("/{tagId}")
    public ResponseEntity<SuccessResponse<?>> deleteTag(
            @Authorization final AuthorizationUser authorizationUser,
            @PathVariable final long tagId
    ){
        tagDeleteUseCase.delete(TagDeleteCommand.of(authorizationUser.memberId(), tagId));
        return SuccessResponse.of(
                HttpStatus.OK,
                "태그 삭제 성공"
        );
    }
}
