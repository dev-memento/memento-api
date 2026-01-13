package com.official.memento.tag.service.usecase;

import com.official.memento.tag.service.command.TagCreateCommand;
import com.official.memento.tag.service.result.TagResult;

@FunctionalInterface
public interface TagCreateUseCase {
    TagResult create(TagCreateCommand command);
}
