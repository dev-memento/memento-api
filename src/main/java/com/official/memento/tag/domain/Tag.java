package com.official.memento.tag.domain;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.tag.domain.enums.TagColor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Tag {
    private final Long id;
    private final String name;
    private final TagColor color;
    private final Long memberId;

    @Builder(toBuilder = true)
    private Tag(final Long id, final String name, final TagColor color, final Long memberId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.memberId = memberId;
    }

    // 새로운 태그를 생성할 때 (ID가 없는 상태)
    public static Tag of(final String name, final TagColor color, final Long memberId) {
        return Tag.builder()
                .name(name)
                .color(color)
                .memberId(memberId)
                .build();
    }

    // 이미 존재하는 태그를 불러올 때 (ID가 있는 상태)
    public static Tag withId(final Long id, final String name, final TagColor color, final Long memberId) {
        return Tag.builder()
                .id(id)
                .name(name)
                .color(color)
                .memberId(memberId)
                .build();
    }

    public Tag update(final String name, final TagColor color) {
        return this.toBuilder()
                .name(name)
                .color(color)
                .build();
    }

    public void checkOwn(final long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new IllegalArgumentException("해당 태그를 소유하지 않음");
        }
    }

    public void validateNotDefaultTag() {
        if ("Untitled".equals(this.name) && this.color== TagColor.GRAY05) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }
}
