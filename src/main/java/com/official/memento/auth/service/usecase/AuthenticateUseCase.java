package com.official.memento.auth.service.usecase;

import com.official.memento.auth.service.AuthResult;
import com.official.memento.auth.service.command.AuthCommand;

@FunctionalInterface
public interface AuthenticateUseCase {
    AuthResult authenticate(AuthCommand command);
}
