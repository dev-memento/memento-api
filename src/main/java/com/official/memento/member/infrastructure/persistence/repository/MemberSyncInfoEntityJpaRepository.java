package com.official.memento.member.infrastructure.persistence.repository;

import com.official.memento.member.infrastructure.persistence.entity.MemberSyncInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSyncInfoEntityJpaRepository extends JpaRepository<MemberSyncInfoEntity,Long> {

    MemberSyncInfoEntity findByMemberId(final long memberId);

}
