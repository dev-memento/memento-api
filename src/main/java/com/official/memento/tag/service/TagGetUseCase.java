package com.official.memento.tag.service;

import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.enums.TagColor;

import java.util.List;

public interface TagGetUseCase {
    List<Tag> getTags(final long memberId);

    Tag findById(final long tagId);
    Tag findByMemberIdAndTagColor(final long memberId, final TagColor tagColor);
}
