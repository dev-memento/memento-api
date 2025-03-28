package com.official.memento.member.infrastructure.persistence.entity;

import com.official.memento.global.entity.BaseTimeEntity;
import com.official.memento.member.domain.MemberSyncInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSyncInfoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long memberId;

    private boolean appleSync;

    private String googleSyncToken;

    private String googleRefreshToken;


    public static MemberSyncInfoEntity of(final MemberSyncInfo memberSyncInfo) {
        return new MemberSyncInfoEntity(null, memberSyncInfo.getMemberId(), memberSyncInfo.isAppleSync(), memberSyncInfo.getGoogleSyncToken(), memberSyncInfo.getGoogleRefreshToken());
    }
}
