package com.official.memento.tag.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.schedule.domain.ScheduleRepository;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.command.TagCreateCommand;
import com.official.memento.tag.service.command.TagDeleteCommand;
import com.official.memento.tag.service.command.TagUpdateCommand;
import com.official.memento.todo.domain.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService implements TagCreateUseCase, TagGetUseCase, TagUpdateUseCase, TagDeleteUseCase {

    private final TagRepository tagRepository;
    private final ScheduleRepository scheduleRepository;
    private final ToDoRepository toDoRepository;

    @Override
    @Transactional
    public Tag create(final TagCreateCommand command) {
        validateDuplicateTagName(command.memberId(), command.name());
        final Tag tag = Tag.of(command.name(), command.color(), command.memberId());
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void update(final TagUpdateCommand command) {
        Tag tag = tagRepository.findById(command.tagId());
        tag.checkOwn(command.memberId());
        validateDuplicateTagName(command.memberId(), command.name());

        tag = tag.update(
                command.name(),
                command.color()
        );

        tagRepository.update(tag);
    }

    @Override
    @Transactional
    public void delete(final TagDeleteCommand command){
        Tag tag = tagRepository.findById(command.tagId());
        tag.checkOwn(command.memberId());

        validateNotDefaultTag(tag);

        Tag defaultTag = tagRepository.findDefaultTag(command.memberId());

        scheduleRepository.updateTagForSchedules(tag.getId(), defaultTag.getId());

        tagRepository.deleteById(tag.getId());
    }

    @Override
    public void deleteAllByMemberId(final long memberId) {
        tagRepository.deleteAllByMemberId(memberId);
    }

    private void validateNotDefaultTag(final Tag tag) {
        if ("Untitled".equals(tag.getName()) && tag.getColor() == TagColor.GRAY05) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }

    private void validateDuplicateTagName(Long memberId, String name) {
        if (tagRepository.existsByMemberIdAndName(memberId, name)) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tag> getTags(final long memberId) {
        return tagRepository.findAllByMemberIdOrderById(memberId);
    }

    @Transactional(readOnly = true)
    @Override
    public Tag findById(final long tagId){
        return tagRepository.findById(tagId);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findByMemberIdAndTagColor(final long memberId, final TagColor tagColor) {
        return tagRepository.findByMemberIdAndTagColor(memberId, tagColor);
    }
}
