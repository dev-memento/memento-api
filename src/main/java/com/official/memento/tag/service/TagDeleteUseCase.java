package com.official.memento.tag.service;

import com.official.memento.tag.service.command.TagDeleteCommand;

@FunctionalInterface
public interface TagDeleteUseCase {
    void delete(final TagDeleteCommand command);
}
