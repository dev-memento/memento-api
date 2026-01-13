package com.official.memento.tag.service.usecase;

import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.result.TagResult;

import java.util.List;

public interface TagGetUseCase {
    List<TagResult> getTags(final long memberId);

    TagResult findById(final long tagId);
    TagResult findByMemberIdAndTagColor(final long memberId, final TagColor tagColor);
}
