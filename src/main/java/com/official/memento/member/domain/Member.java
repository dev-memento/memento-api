package com.official.memento.member.domain;

import com.official.memento.global.entity.BaseTimeEntity;

import java.time.LocalDateTime;

public class Member extends BaseTimeEntity {
    private final Long id;

    public Member(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member createNew() {
        LocalDateTime now = LocalDateTime.now();
        return new Member(null, now, now);
    }

    public static Member withId(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Member(id, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Member withUpdatedTimestamps(LocalDateTime updatedAt) {
        return new Member(this.id, this.createdAt, updatedAt);
    }
}
