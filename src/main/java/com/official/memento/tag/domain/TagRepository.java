package com.official.memento.tag.domain;

import com.official.memento.tag.domain.enums.TagColor;

import java.util.List;

public interface TagRepository {
    Tag save(Tag tag);

    Tag findById(Long id);

    List<Tag> findAllByMemberId(Long memberId);

    Tag findByMemberIdAndTagColor(Long memberId, TagColor tagColor);
}
