package com.official.memento.tag.domain;

import com.official.memento.tag.domain.enums.TagColor;

import java.util.List;

public interface TagRepository {
    Tag save(final Tag tag);

    Tag update(final Tag tag);

    Tag findById(final Long id);

    List<Tag> findAllByMemberId(final Long memberId);

    Tag findByMemberIdAndTagColor(final Long memberId, TagColor tagColor);
}
