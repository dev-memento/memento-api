package com.official.memento.tag.service.usecase;

import com.official.memento.tag.service.command.TagUpdateCommand;
import com.official.memento.tag.service.result.TagResult;

@FunctionalInterface
public interface TagUpdateUseCase {
    TagResult update(final TagUpdateCommand command);
}
