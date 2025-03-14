package com.official.memento.tag.service;

import com.official.memento.schedule.domain.entity.Schedule;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.service.command.TagCreateCommand;
import com.official.memento.tag.service.command.TagUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService implements TagCreateUseCase, TagGetUseCase, TagUpdateUseCase {

    private final TagRepository tagRepository;

    @Override
    @Transactional
    public Tag create(final TagCreateCommand command) {
        final Tag tag = Tag.of(command.name(), command.color(), command.memberId());
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void update(final TagUpdateCommand command) {
        Tag tag = tagRepository.findById(command.tagId());
        checkOwn(command.memberId(), tag);

        tag = tag.update(
                command.name(),
                command.color()
        );

        tagRepository.update(tag);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Tag> getTags(final long memberId) {
        return tagRepository.findAllByMemberId(memberId);
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

    private static void checkOwn(final long memberId, final Tag tag) {
        if (tag.getMemberId() != memberId) {
            throw new IllegalArgumentException("해당 태그를 소유하지 않음");
        }
    }
}
