package com.official.memento.tag.infrastructure.persistence;

import com.official.memento.tag.domain.enums.TagColor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {
    @Query(
            value = "SELECT * FROM tag WHERE member_id = :memberId",
            nativeQuery = true
    )
    List<TagEntity> findAllByMemberId(Long memberId);

    Optional<TagEntity> findByMemberIdAndColor(Long memberId, TagColor color);

    @Query(
            value = "SELECT * FROM tag WHERE member_id = :memberId AND name = 'Untitled' AND color = 'GRAY05'",
            nativeQuery = true
    )
    Optional<TagEntity> findDefaultTag(Long memberId);

    void deleteAllByMemberId(final long memberId);
}
