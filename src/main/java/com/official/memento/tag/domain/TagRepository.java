package com.official.memento.tag.domain;

import com.official.memento.tag.domain.enums.TagColor;

import java.util.List;

public interface TagRepository {
    Tag save(final Tag tag);

    Tag update(final Tag tag);

    void deleteById(final long tagId);

    void deleteAllByMemberId(final long memberId);

    Tag findById(final Long id);

    List<Tag> findAllByMemberIdOrderById(final Long memberId);

    Tag findByMemberIdAndTagColor(final Long memberId, final TagColor tagColor);

    Tag findDefaultTag(final Long memberId);

    boolean existsByMemberIdAndName(final Long memberId, final String name);
}
