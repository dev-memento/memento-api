package com.official.memento.tag.service;

import com.official.memento.tag.service.command.TagUpdateCommand;

@FunctionalInterface
public interface TagUpdateUseCase {
    void update(final TagUpdateCommand command);
}
