package com.official.memento.tag.service;

import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.service.command.TagUpdateCommand;

@FunctionalInterface
public interface TagUpdateUseCase {
    Tag update(final TagUpdateCommand command);
}
