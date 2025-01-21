package com.official.memento.todo.infrastructure.persistence;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.tag.infrastructure.persistence.TagEntity;
import com.official.memento.todo.domain.ToDoTag;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo_tag")
public class ToDoTagEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tagId;
    private Long toDoId;

    private ToDoTagEntity(
            final Long id,
            final Long tagId,
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

    private ToDoTagEntity(
            final long tagId,
            final long toDoId
    ) {
        this.tagId = tagId;
        this.toDoId = toDoId;
    }

    protected ToDoTagEntity() {
    }


    public static ToDoTagEntity of(final ToDoTag toDoTag) {
        return new ToDoTagEntity(
                toDoTag.getTagId(),
                toDoTag.getToDoId()
        );
    }

    public static ToDoTagEntity withId(final ToDoTag toDoTag) {
        return new ToDoTagEntity(
                toDoTag.getId(),
                toDoTag.getTagId(),
                toDoTag.getToDoId(),
                toDoTag.getCreatedAt(),
                toDoTag.getUpdatedAt()
        );
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
