package com.official.memento.tag.infrastructure.persistence;

import com.official.memento.tag.domain.enums.TagColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {
    @Query(
            value = "SELECT * FROM tags WHERE member_id = :memberId",
            nativeQuery = true
    )
    List<TagEntity> findAllByMemberId(Long memberId);

    TagEntity findByMemberIdAndColor(Long memberId, TagColor color);
}
