package com.official.memento.auth.service.usecase;

import com.official.memento.auth.service.command.AuthCommand;
import com.official.memento.auth.service.result.NewAuthResult;

@FunctionalInterface
public interface AuthenticateUseCase {
    NewAuthResult authenticate(AuthCommand command);
}
