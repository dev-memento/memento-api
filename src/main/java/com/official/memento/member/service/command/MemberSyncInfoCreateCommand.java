package com.official.memento.member.service.command;

public record MemberSyncInfoCreateCommand(
        long memberId
) {
    public static MemberSyncInfoCreateCommand from(final long memberId){
        return new MemberSyncInfoCreateCommand(memberId);
    }
}
