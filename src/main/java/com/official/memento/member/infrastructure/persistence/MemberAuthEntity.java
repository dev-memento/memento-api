package com.official.memento.member.infrastructure.persistence;

import com.official.memento.auth.domain.AuthProvider;
import jakarta.persistence.*;

@Entity
@Table(name = "member_auth", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"platformId", "provider"})})
public class MemberAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long memberId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false)
    private String platformId;

    @Column(nullable = false)
    private String refreshToken;

    protected MemberAuthEntity() {
    }

    public MemberAuthEntity(
            final Long id,
            final AuthProvider provider,
            final String platformId,
            final String refreshToken,
            final long memberId) {
        this.id = id;
        this.provider = provider;
        this.platformId = platformId;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getMemberId() {
        return memberId;
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }
}
