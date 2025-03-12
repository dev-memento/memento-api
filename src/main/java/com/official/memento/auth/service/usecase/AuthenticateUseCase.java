package com.official.memento.auth.service.usecase;

import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.dto.MemberInfoDto;

@FunctionalInterface
public interface AuthenticateUseCase {
    MemberInfoDto authenticate(AuthCommand command);
}
