package com.official.memento.tag.service;

import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.InvalidRequestBodyException;
import com.official.memento.schedule.service.command.SchedulesTagUpdateCommand;
import com.official.memento.schedule.service.usecase.ScheduleUpdateUseCase;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.command.TagCreateCommand;
import com.official.memento.tag.service.command.TagDeleteCommand;
import com.official.memento.tag.service.command.TagUpdateCommand;
import com.official.memento.tag.service.result.TagResult;
import com.official.memento.tag.service.usecase.TagCreateUseCase;
import com.official.memento.tag.service.usecase.TagDeleteUseCase;
import com.official.memento.tag.service.usecase.TagGetUseCase;
import com.official.memento.tag.service.usecase.TagUpdateUseCase;
import com.official.memento.todo.service.command.TodosTagUpdateCommand;
import com.official.memento.todo.service.usecase.ToDoUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService implements TagCreateUseCase, TagGetUseCase, TagUpdateUseCase, TagDeleteUseCase {

    private final TagRepository tagRepository;
    private final ScheduleUpdateUseCase scheduleUpdateUseCase;
    private final ToDoUpdateUseCase toDoUpdateUseCase;

    @Override
    @Transactional
    public TagResult create(final TagCreateCommand command) {
        validateDuplicateTagName(command.memberId(), command.name());
        final Tag tag = Tag.of(command.name(), command.color(), command.memberId());
        return TagResult.of(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public TagResult update(final TagUpdateCommand command) {
        Tag tag = tagRepository.findById(command.tagId());
        tag.checkOwn(command.memberId());
        if(!tag.getName().equals(command.name())) {
            validateDuplicateTagName(command.memberId(), command.name());
        }
        tag = tag.update(
                command.name(),
                command.color()
        );

        return TagResult.of(tagRepository.update(tag));
    }

    @Override
    @Transactional
    public void delete(final TagDeleteCommand command){
        Tag tag = tagRepository.findById(command.tagId());
        tag.checkOwn(command.memberId());

        //삭제한 태그가 기본(삭제불가)태그인지 확인
        tag.validateNotDefaultTag();

        Tag defaultTag = tagRepository.findDefaultTag(command.memberId());

        //기존 태그와 연결되어 있는 스케줄과 투두의 태그 업데이트
        scheduleUpdateUseCase.updateSchedulesTag(SchedulesTagUpdateCommand.of(tag.getId(), defaultTag.getId()));
        toDoUpdateUseCase.updateTodosTag(TodosTagUpdateCommand.of(tag.getId(),defaultTag.getId()));

        tagRepository.deleteById(tag.getId());
    }

    @Override
    public void deleteAllByMemberId(final long memberId) {
        tagRepository.deleteAllByMemberId(memberId);
    }


    private void validateDuplicateTagName(Long memberId, String name) {
        if (tagRepository.existsByMemberIdAndName(memberId, name)) {
            throw new InvalidRequestBodyException(ErrorCode.INVALID_JSON_FORMAT);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TagResult> getTags(final long memberId) {
        return tagRepository.findAllByMemberIdOrderById(memberId).stream()
                .map(TagResult::of)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TagResult findById(final long tagId){
        return TagResult.of(tagRepository.findById(tagId));
    }

    @Override
    @Transactional(readOnly = true)
    public TagResult findByMemberIdAndTagColor(final long memberId, final TagColor tagColor) {
        return TagResult.of(tagRepository.findByMemberIdAndTagColor(memberId, tagColor));
    }
}
