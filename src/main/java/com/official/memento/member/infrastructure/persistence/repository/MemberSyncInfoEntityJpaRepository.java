package com.official.memento.member.infrastructure.persistence.repository;

import com.official.memento.member.infrastructure.persistence.entity.MemberSyncInfoEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSyncInfoEntityJpaRepository extends JpaRepository<MemberSyncInfoEntity,Long> {

    Optional<MemberSyncInfoEntity> findByMemberId(final long memberId);

    void deleteByMemberId(final long memberId);
}
