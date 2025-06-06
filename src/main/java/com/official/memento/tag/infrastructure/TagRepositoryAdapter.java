package com.official.memento.tag.infrastructure;

import static com.official.memento.global.exception.ErrorCode.NOT_FOUND_ENTITY;

import com.official.memento.global.exception.EntityNotFoundException;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.tag.domain.Tag;
import com.official.memento.tag.domain.TagRepository;
import com.official.memento.tag.domain.enums.TagColor;
import com.official.memento.tag.infrastructure.persistence.TagEntity;
import com.official.memento.tag.infrastructure.persistence.TagJpaRepository;

import java.util.List;

@Adapter
public class TagRepositoryAdapter implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    public TagRepositoryAdapter(TagJpaRepository tagJpaRepository) {
        this.tagJpaRepository = tagJpaRepository;
    }

    @Override
    public Tag save(Tag tag) {
        TagEntity entity = TagEntity.of(
                tag.getName(),
                tag.getColor(),
                tag.getMemberId()
        );
        TagEntity savedEntity = tagJpaRepository.save(entity);
        return Tag.withId(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getColor(),
                savedEntity.getMemberId()
        );
    }

    @Override
    public Tag update(final Tag tag) {
        TagEntity updatedEntity = tagJpaRepository.save(TagEntity.withId(tag));
        return Tag.withId(
                updatedEntity.getId(),
                updatedEntity.getName(),
                updatedEntity.getColor(),
                updatedEntity.getMemberId()
        );
    }

    @Override
    public void deleteById(final long tagId){
        tagJpaRepository.deleteById(tagId);
    }

    @Override
    public void deleteAllByMemberId(final long memberId) {
        tagJpaRepository.deleteAllByMemberId(memberId);
    }

    @Override
    public Tag findById(Long id) {
        final TagEntity entity = tagJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ENTITY));
        return Tag.withId(
                entity.getId(),
                entity.getName(),
                entity.getColor(),
                entity.getMemberId()
        );
    }

    @Override
    public List<Tag> findAllByMemberIdOrderById(Long memberId) {
        return tagJpaRepository.findAllByMemberIdOrderById(memberId)
                .stream()
                .map(entity -> Tag.withId(
                        entity.getId(),
                        entity.getName(),
                        entity.getColor(),
                        entity.getMemberId()
                ))
                .toList();
    }

    @Override
    public Tag findByMemberIdAndTagColor(Long memberId, TagColor tagColor) {
        TagEntity entity = tagJpaRepository.findByMemberIdAndColor(memberId, tagColor)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ENTITY));
        return Tag.withId(
                entity.getId(),
                entity.getName(),
                entity.getColor(),
                entity.getMemberId()
        );
    }

    @Override
    public Tag findDefaultTag(final Long memberId){
        TagEntity entity = tagJpaRepository.findDefaultTag(memberId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ENTITY));
        return Tag.withId(
                entity.getId(),
                entity.getName(),
                entity.getColor(),
                entity.getMemberId()
        );
    }
}
