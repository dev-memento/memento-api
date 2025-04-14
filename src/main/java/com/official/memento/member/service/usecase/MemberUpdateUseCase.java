package com.official.memento.member.service.usecase;

import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.result.NewAuthResult;

public interface MemberUpdateUseCase {
    NewAuthResult authenticate(final AuthCommand command);
}