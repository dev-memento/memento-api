package com.official.memento.auth.service;

import com.official.memento.auth.domain.AccessToken;
import com.official.memento.auth.domain.AuthorizationMember;
import com.official.memento.auth.domain.RefreshToken;
import com.official.memento.member.domain.Member;
import com.official.memento.member.infrastructure.persistence.MemberEntity;

public class AuthResult {
    private final AccessToken accessToken;
    private final RefreshToken refreshToken;
    private final Member member;
    private final boolean isNewUser;

    public AuthResult(AccessToken accessToken, RefreshToken refreshToken, Member member, boolean isNewUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.member = member;
        this.isNewUser = isNewUser;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public Member getMember() {
        return member;
    }

    public boolean isNewUser() {
        return isNewUser;
    }
}