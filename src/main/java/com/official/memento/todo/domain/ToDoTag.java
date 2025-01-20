package com.official.memento.todo.domain;

import com.official.memento.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

public class ToDoTag extends BaseTimeEntity {
    private Long id;
    private long tagId;
    private long toDoId;

    public ToDoTag(
            final Long id,
            final long tagId,
            final long toDoId,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        this.id = id;
        this.tagId = tagId;
        this.toDoId = toDoId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ToDoTag(final long tagId, final long toDoId) {
        this.tagId = tagId;
        this.toDoId = toDoId;
    }

    public static ToDoTag withId(
            final Long id,
            final long tagId,
            final long toDoId,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        return new ToDoTag(id, tagId, toDoId, createdAt, updatedAt);
    }

    public static ToDoTag of(
            final long tagId,
            final long toDoId
    ) {
        return new ToDoTag(tagId, toDoId);
    }

    public void updateTag(
            final long tagId,
            final LocalDateTime updatedAt
    ) {
        this.tagId = tagId;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public long getTagId() {
        return tagId;
    }

    public long getToDoId() {
        return toDoId;
    }
}
