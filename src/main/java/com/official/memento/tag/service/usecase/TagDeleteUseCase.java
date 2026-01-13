package com.official.memento.tag.service.usecase;

import com.official.memento.tag.service.command.TagDeleteCommand;

public interface TagDeleteUseCase {
    void delete(final TagDeleteCommand command);

    void deleteAllByMemberId(final long memberId);
}
